package com.reyco.dasbx.gateway.core.utils;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;
import com.reyco.dasbx.gateway.core.model.R;

import reactor.core.publisher.Mono;

/**
 * 请求工具类
 * @author reyco
 *
 */
public class RequestUtils {
	/**
	 * 获取请求路径
	 * @param request
	 * @return
	 */
	public static String getPath(ServerHttpRequest request) {
		return request.getPath().toString();
	}
	/**
	 * 获取请求地址
	 * @param request
	 * @return
	 */
	public static String getURL(ServerHttpRequest request) {
		return request.getURI().toString();
	}
	/**
	 * 获取option
	 * @param request
	 * @param optionName
	 * @return
	 */
	public static String getOption(ServerHttpRequest request,String optionName) {
		String option = getHeader(request, optionName);
		if(StringUtils.isBlank(option)) {
			option = getCookie(request, optionName);
		}
		return option;
	}
	/**
	 * 获取paramter
	 * @param request
	 * @param parameterName
	 * @return
	 */
	public static String getParameter(ServerHttpRequest request,String parameterName) {
		MultiValueMap<String, String> multiValueMap = request.getQueryParams();
		return multiValueMap.getFirst(parameterName);
	}
	/**
	 * 获取header
	 * @param request
	 * @param headerName
	 * @return
	 */
	public static String getHeader(ServerHttpRequest request,String headerName) {
		HttpHeaders headers = request.getHeaders();
		return headers.getFirst(headerName);
	}
	/**
	 * 获取cookie
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookie(ServerHttpRequest request,String cookieName) {
		HttpCookie httpCookie = request.getCookies().getFirst(cookieName);
		String cookie = null;
		if(httpCookie!=null) {
			cookie = httpCookie.getValue();
		}
		return cookie;
	}
	/**
	 * 响应错误
	 * @param exchange
	 * @param status
	 * @param message
	 * @return
	 */
	public static Mono<Void> response(ServerWebExchange exchange, R r) {
		exchange.getResponse().setStatusCode(HttpStatus.OK);
		exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
		DataBuffer wrap = dataBufferFactory.wrap(JSON.toJSONString(r).getBytes(StandardCharsets.UTF_8));
		return exchange.getResponse().writeWith(Mono.just(wrap));
	}
	
}
