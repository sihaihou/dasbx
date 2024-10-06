package com.reyco.dasbx.config.web.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.reyco.dasbx.commons.utils.DictionaryOrderUtils;
import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.model.constants.MethodType;
import com.reyco.dasbx.trim.requset.RemoveSpaceHttpServletRequestWrapper;

@Component
public class SignInterceptor extends HandlerInterceptorAdapter {
	public final static String INTERCEPTOR_NAME = "sign";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		Map<String, Object> requestParamMap = getRequestParams(request);
		if(requestParamMap!=null && requestParamMap.size()>0) {
			String lowestDictionaryOrderString = DictionaryOrderUtils.lowestDictionaryOrderString(requestParamMap);
			System.out.println(lowestDictionaryOrderString);
		}
		return true;
	}
	/**
	 * 获取请求参数 HttpServletRequest\HttpServletResponse\MultipartFile\Model
	 * @param joinPoint
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private Map<String,Object> getRequestParams(HttpServletRequest request) throws IOException {
		Map<String,Object> paramterMap = new HashMap();
		if (request.getMethod().equalsIgnoreCase(MethodType.GET.getMethod())) {
			String parameters = request.getQueryString();
			if(StringUtils.isBlank(parameters)) {
				return paramterMap;
			}
			StringTokenizer stringTokenizer = new StringTokenizer(parameters, "&");
			while(stringTokenizer.hasMoreTokens()) {
				String parameter = stringTokenizer.nextToken();
				String[] params = parameter.split("=");
				if(params.length==2) {
					paramterMap.put(params[0], params[1]);
				}
			}
		} else {
			RemoveSpaceHttpServletRequestWrapper remove = (RemoveSpaceHttpServletRequestWrapper) request;
			String body = remove.getBody();
			if(StringUtils.isBlank(body)) {
				return paramterMap;
			}
			Map<String, Object> jsonToMap = JsonUtils.jsonToMap(body);
			return jsonToMap;
		}
		return paramterMap;
	}
	
	
	
	public static void main(String[] args) {
		String parameters = "b=2&c=3&a=&d=4";
		Map<String,Object> paramterMap = new HashMap();
		StringTokenizer stringTokenizer = new StringTokenizer(parameters, "&");
		while(stringTokenizer.hasMoreTokens()) {
			String parameter = stringTokenizer.nextToken();
			String[] params = parameter.split("=");
			if(params.length==2) {
				paramterMap.put(params[0], params[1]);
			}
		}
		String body = "{\"c\":\"3\",\"d\":\"4\",\"a\":\"\",\"b\":\"2\"}";
		Map<String, Object> jsonToMap = JsonUtils.jsonToMap(body);
		String lowestDictionaryOrderString = DictionaryOrderUtils.lowestDictionaryOrderString(paramterMap);
		System.out.println(lowestDictionaryOrderString);
		String jsonToMap1 = DictionaryOrderUtils.lowestDictionaryOrderString(jsonToMap);
		System.out.println(jsonToMap1);
	}
}
