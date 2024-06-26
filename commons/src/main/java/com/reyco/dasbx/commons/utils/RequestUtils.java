package com.reyco.dasbx.commons.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * @author reyco
 * @date 2022.03.15
 * @version v1.0.1
 */
public class RequestUtils {
	/**
	 * 获取request对象
	 * @return
	 */
	public static HttpServletRequest getHttpServletRequest() {
		 return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 必须参数
	 * @param req
	 * @param paramName
	 * @return
	 */
	public static String required(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("Param '" + paramName + "' is required.");
		}
		String encoding = request.getParameter("encoding");
		if (!StringUtils.isBlank(encoding)) {
			try {
				value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
			} catch (UnsupportedEncodingException ignore) {
			}
		}
		return value.trim();
	}

	/**
	 * 可选参数
	 * @param req
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String optional(HttpServletRequest request, String key, String defaultValue) {
		if (!request.getParameterMap().containsKey(key) || request.getParameterMap().get(key)[0] == null) {
			return defaultValue;
		}
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		String encoding = request.getParameter("encoding");
		if (!StringUtils.isBlank(encoding)) {
			try {
				value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
			} catch (UnsupportedEncodingException ignore) {
			}
		}
		return value.trim();
	}
	/**
	 * Accept-Charset
	 * @param req
	 * @return
	 */
	public static String getAcceptEncoding(HttpServletRequest request) {
		String encode = StringUtils.defaultIfEmpty(request.getHeader("Accept-Charset"), "UTF-8");
		encode = encode.contains(",") ? encode.substring(0, encode.indexOf(",")) : encode;
		return encode.contains(";") ? encode.substring(0, encode.indexOf(";")) : encode;
	}

	/**
	 * User-Agent
	 * @param request
	 * @return
	 */
	public static String getUserAgent(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (StringUtils.isEmpty(userAgent)) {
			userAgent = StringUtils.defaultIfEmpty(request.getHeader("Client-Version"),StringUtils.EMPTY);
		}
		return userAgent;
	}
	/**
	 * 必须header
	 * @param request
	 * @param headerName
	 * @return
	 */
	public static String getHeaderRequired(HttpServletRequest request,String headerName) {
		String value = request.getHeader(headerName);
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("header '" + headerName + "' is required.");
		}
		String encoding = request.getParameter("encoding");
		if (!StringUtils.isBlank(encoding)) {
			try {
				value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
			} catch (UnsupportedEncodingException ignore) {
			}
		}
		return value.trim();
	}
	/**
	 * 可选header
	 * @param request
	 * @param headerName
	 * @return
	 */
	public static String getHeaderOptional(HttpServletRequest request,String headerName, String defaultValue) {
		String value = request.getHeader(headerName);
		if(StringUtils.isBlank(value)) {
			return defaultValue;
		}
		String encoding = request.getParameter("encoding");
		if (StringUtils.isNotBlank(encoding)) {
			try {
				value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
			} catch (UnsupportedEncodingException ignore) {
			}
		}
		return value.trim();
	}
	/**
	 * 必须Cookie
	 * @param request
	 * @param headerName
	 * @return
	 */
	public static String getCookieRequired(HttpServletRequest request,String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(cookies==null) {
			throw new IllegalArgumentException("Cookie '" + cookieName + "' is required.");
		}
		String value = "";
		for(int i=0;i<cookies.length;i++) {
			Cookie cookie = cookies[i];
			if(cookie.getName().equalsIgnoreCase(cookieName)) {
				value = cookie.getValue();
			}
		}
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("Cookie '" + cookieName + "' is required.");
		}
		String encoding = request.getParameter("encoding");
		if (!StringUtils.isBlank(encoding)) {
			try {
				value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
			} catch (UnsupportedEncodingException ignore) {
			}
		}
		return value.trim();
	}
	/**
	 * 可选Cookie
	 * @param request
	 * @param headerName
	 * @return
	 */
	public static String getCookieOptional(HttpServletRequest request,String cookieName, String defaultValue) {
		Cookie[] cookies = request.getCookies();
		if(cookies==null) {
			return defaultValue;
		}
		String value = "";
		for(int i=0;i<cookies.length;i++) {
			Cookie cookie = cookies[i];
			if(cookie.getName().equalsIgnoreCase(cookieName)) {
				value = cookie.getValue();
			}
		}
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		String encoding = request.getParameter("encoding");
		if (!StringUtils.isBlank(encoding)) {
			try {
				value = new String(value.getBytes(StandardCharsets.UTF_8), encoding);
			} catch (UnsupportedEncodingException ignore) {
			}
		}
		return value.trim();
	}
	/**
	 * 获取Token:先从header中获取token属性，如果header中没有，在从cookie中获取Token，如果cookie中也没有，返回null
	 * @param request
	 * @param optionName
	 * @return
	 */
	public static String getTokenByHeaderAndCookie(HttpServletRequest request, String tokenName) {
		String token = RequestUtils.getHeaderOptional(request, tokenName, null);
		if (token == null) {
			token = RequestUtils.getCookieOptional(request, tokenName, null);
		}
		return token;
	}
}
