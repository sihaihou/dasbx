package com.reyco.dasbx.trim.requset;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RemoveSpaceHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private Map<String, String[]> params;
	private byte[] cachedJsonInputStream;
	private String body;
	private String trimBody;

	public RemoveSpaceHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.params = copyAndTrimParameters(request.getParameterMap());
	}

	private Map<String, String[]> copyAndTrimParameters(Map<String, String[]> source) {
		Map<String, String[]> result = new LinkedHashMap<String, String[]>();

		if (source == null || source.isEmpty()) {
			return result;
		}

		for (Map.Entry<String, String[]> entry : source.entrySet()) {
			String[] sourceValues = entry.getValue();
			if (sourceValues == null) {
				result.put(entry.getKey(), null);
				continue;
			}

			// 必须复制数组，不能修改原始请求中的参数数组。
			String[] values = sourceValues.clone();
			for (int i = 0; i < values.length; i++) {
				if (values[i] != null) {
					values[i] = values[i].trim();
				}
			}
			result.put(entry.getKey(), values);
		}

		return result;
	}

	@Override
	public String getParameter(String name) {
		String[] values = params.get(name);
		if (values == null || values.length == 0) {
			return null;
		}
		return values[0];
	}

	@Override
	public String[] getParameterValues(String name) {
		 String[] values = params.get(name);
	     return values == null ? null : values.clone();
	}
	
	@Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String[] values = entry.getValue();
            result.put(entry.getKey(),values == null ? null : values.clone());
        }

        return Collections.unmodifiableMap(result);
    }

	/**
	 * 重写getInputStream方法 post类型的请求参数必须通过流才能获取到值
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		// 已经处理过一次时直接返回缓存，避免 InputStream 被重复读取后为空。
		if (cachedJsonInputStream != null) {
			return toServletInputStream(cachedJsonInputStream);
		}

		// 非json类型，直接返回
		String contentType = super.getHeader(HttpHeaders.CONTENT_TYPE);
		if (!isJsonContentType(contentType)) {
			// 文件上传等非 JSON 请求继续使用原始流，避免整体加载进内存。
			return super.getInputStream();
		}

		// 获取源字符集
		Charset charset = resolveCharset(contentType);
		byte[] requestBody = StreamUtils.copyToByteArray(super.getInputStream());
		this.body = new String(requestBody, charset);

		if (StringUtils.isEmpty(this.body)) {
			this.trimBody = this.body;
			this.cachedJsonInputStream = requestBody;
			return toServletInputStream(this.cachedJsonInputStream);
		}
		
		String jsonSource = this.body;
        if (jsonSource.startsWith("\uFEFF")) {
            jsonSource = jsonSource.substring(1);
        }
        
        try {
            Object json = JSON.parse(jsonSource);
            Object trimmedJson = trimJsonValue(json);

            this.trimBody = JSON.toJSONString(trimmedJson);
            this.cachedJsonInputStream = this.trimBody.getBytes(charset);
        } catch (RuntimeException e) {
            // 非法 JSON 保持原始内容，避免改变下游请求语义。
            this.trimBody = this.body;
            this.cachedJsonInputStream = requestBody;
        }

		return toServletInputStream(this.cachedJsonInputStream);
	}
	 @Override
    public BufferedReader getReader() throws IOException {
        String contentType = super.getHeader(HttpHeaders.CONTENT_TYPE);

        if (!isJsonContentType(contentType)) {
            return super.getReader();
        }

        return new BufferedReader(new InputStreamReader(getInputStream(),resolveCharset(contentType)));
    }

	private ServletInputStream toServletInputStream(final byte[] bodyBytes) {
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(bodyBytes);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return inputStream.read();
			}

			@Override
			public int read(byte[] bytes, int offset, int length) throws IOException {
				return inputStream.read(bytes, offset, length);
			}

			@Override
			public void setReadListener(ReadListener listener) {
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public boolean isFinished() {
				return inputStream.available() == 0;
			}
		};
	}

	/**
	 * 支持 application/json、application/json;charset=UTF-8 以及
	 * application/problem+json 等 JSON 子类型。
	 */
	private boolean isJsonContentType(String contentType) {
		if (StringUtils.isBlank(contentType)) {
			return false;
		}
		try {
			MediaType mediaType = MediaType.parseMediaType(contentType);
			String subtype = mediaType.getSubtype().toLowerCase(Locale.ROOT);
			return MediaType.APPLICATION_JSON.isCompatibleWith(mediaType) || subtype.endsWith("+json");
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * 优先使用 Content-Type 中的字符集，其次使用请求字符集，最后使用 UTF-8。
	 */
	private Charset resolveCharset(String contentType) {
		try {
			Charset charset = MediaType.parseMediaType(contentType).getCharset();
			if (charset != null) {
				return charset;
			}
		} catch (IllegalArgumentException e) {
			// 继续使用请求字符集或默认 UTF-8。
		}

		String encoding = super.getCharacterEncoding();
		if (StringUtils.isNotBlank(encoding)) {
			try {
				return Charset.forName(encoding);
			} catch (IllegalArgumentException e) {
				// 非法字符集使用 UTF-8。
			}
		}

		return StandardCharsets.UTF_8;
	}
	/**
     * 只去除字符串值的前后空格，不改变数字、布尔值和 null。
     */
    private Object trimJsonValue(Object value) {
        if (value instanceof String) {
            return ((String) value).trim();
        }

        if (value instanceof JSONObject) {
            return trimJsonObject((JSONObject) value);
        }

        if (value instanceof JSONArray) {
            return trimJsonArray((JSONArray) value);
        }

        return value;
    }
    private JSONObject trimJsonObject(JSONObject source) {
        JSONObject result = new JSONObject();

        for (Map.Entry<String, Object> entry : source.entrySet()) {
            result.put(entry.getKey(),trimJsonValue(entry.getValue()));
        }

        return result;
    }
    private JSONArray trimJsonArray(JSONArray source) {
        JSONArray result = new JSONArray(source.size());

        for (int i = 0; i < source.size(); i++) {
            /*
             * 注意：数组中的字符串只做 trim。
             * 不能再次调用 JSON.parse，否则字符串形式的 JSON 文本
             * 会被错误转换成对象或数组。
             */
            result.add(trimJsonValue(source.get(i)));
        }

        return result;
    }

	public Map<String, String[]> getParams() {
		return params;
	}

	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTrimBody() {
		return trimBody;
	}

	public void setTrimBody(String trimBody) {
		this.trimBody = trimBody;
	}
}