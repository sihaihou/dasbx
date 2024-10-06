package com.reyco.dasbx.commons.utils.encrypt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 获取密匙
 * @author reyco
 *
 */
public class SecretKeyUtils {
	
	public static String getSecretKey() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return getSecretKey(request);
	}
	public static String getSecretKey(HttpServletRequest request) {
		//String code = TokenUtils.getCodeString(request);
		//String secretKey = code.substring(0, 16);
		String secretKey = "1234567890123456";
		return secretKey;
	}
	
}
