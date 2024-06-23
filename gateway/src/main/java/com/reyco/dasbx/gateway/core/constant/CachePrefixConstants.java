package com.reyco.dasbx.gateway.core.constant;

public interface CachePrefixConstants {
	/**
	 * 登录用户名
	 */
	String USER_LOGIN_UID = "dasbx:login:uid:";
	/**
	 * token前缀
	 */
	String TOKEN_PREFIX = "dasbx:login:token:";
	/**
	 * 用户与设备绑定
	 */
	String USER_BING_DEVICE_PREFIX = "dasbx:user:bing:device:";
	/**
	 * 设备与用户绑定
	 */
	String DEVICE_BING_USER_PREFIX = "dasbx:device:bing:user:";
	/**
	 * 设备与token绑定
	 */
	String DEVICE_BING_TOKEN_PREFIX = "dasbx:device:bing:token:";
}
