package com.reyco.dasbx.trim.requset;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class RemoveSpaceHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private Map<String, String[]> params = new HashMap<String, String[]>();
	private String body;
	private String trimBody;

	public RemoveSpaceHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.params.putAll(request.getParameterMap());
		this.removeSpaceParameterValues();
	}

	/**
	 * 将params的值去除空格后重写回去
	 */
	private void removeSpaceParameterValues() {
		Set<String> set = params.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String[] values = params.get(key);
			values[0] = values[0].trim();
			params.put(key, values);
		}
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
		return params.get(name);
	}

	/**
	 * 重写getInputStream方法 post类型的请求参数必须通过流才能获取到值
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		// 非json类型，直接返回
		if (!super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
			return super.getInputStream();
		}
		// 为空，直接返回
		String json = new BufferedReader(new InputStreamReader(super.getInputStream())).lines().parallel().collect(Collectors.joining("\n"));
		if (StringUtils.isEmpty(json)) {
			return super.getInputStream();
		}
		this.body = json;
		// json去空格
		Object jsonObject = jsonStrTrim(json);
		String jsonTrim = JSON.toJSONString(jsonObject);
		this.trimBody = jsonTrim;
		// 写回流中
		ByteArrayInputStream bis = new ByteArrayInputStream(jsonTrim.getBytes("utf-8"));
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bis.read();
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
				return true;
			}
		};
	}
	/**
	 * json字符串去空格
	 * @param jsonStr
	 * @return
	 * @throws org.json.JSONException 
	 * @throws JSONException 
	 */
	private Object jsonStrTrim(String jsonStr){
		if (StringUtils.isEmpty(jsonStr)) {
			return "";
		}
		try {
			Object json = JSON.parse(jsonStr);
			if(json instanceof JSONArray) {
				return jsonArrayTrim((JSONArray)json);
			}
			if(json instanceof JSONObject) {
				return jsonObjectTrim((JSONObject)json);
			}
		} catch (Exception e) {
			
		}
		return jsonStr.trim();
	}
	/**
	 * json对象去空格
	 * @param jsonObject
	 * @return
	 */
	private JSONObject jsonObjectTrim(JSONObject jsonObject) {
		Set<Entry<String, Object>> entrySet = jsonObject.entrySet();
		entrySet.forEach(entry-> {
			Object value = entry.getValue();
			if(value==null) {
				return;
			}
			if (value instanceof JSONArray) {
				jsonObject.put(entry.getKey(), jsonArrayTrim((JSONArray) value));
				return;
			}
			if (value instanceof JSONObject) {
				jsonObject.put(entry.getKey(), jsonObjectTrim((JSONObject) value));
				return;
			}
			if (value instanceof String) {
				String resultValue = (String) value;
				if (!StringUtils.isEmpty(resultValue)) {
					resultValue = resultValue.trim();
					jsonObject.put(entry.getKey(), resultValue);
				}
				return;
			}
		});
		return jsonObject;
	}
	/**
	 * json数组对象去空格
	 * @param jsonArray
	 * @return
	 * @throws JSONException 
	 */
	private JSONArray jsonArrayTrim(JSONArray jsonArray){
		if(jsonArray!=null && jsonArray.size()>0) {
			Object tempObject = null;
			for (int i=0;i<jsonArray.size();i++) {
				tempObject = jsonArray.get(i);
				if (tempObject instanceof JSONArray) {
					tempObject = jsonArrayTrim((JSONArray) tempObject);
					jsonArray.set(i, tempObject);
					continue;
				}
				if(tempObject instanceof JSONObject) {
					JSONObject jsonObject = jsonObjectTrim((JSONObject)tempObject);
					jsonArray.set(i, jsonObject);
					continue;
				}
				if(tempObject instanceof String) {
					Object jsonStrTrim = jsonStrTrim((String)tempObject);
					jsonArray.set(i, jsonStrTrim);
				}
			}
		}
		return jsonArray;
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