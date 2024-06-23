package com.reyco.dasbx.model.constants;

public interface CachePrefixConstants {
	/**
	 * 登录用户名
	 */
	String USER_LOGIN_UID = "dasbx:login:uid:";
	/**
	 * token前缀
	 */
	String TOKEN_PREFIX = "dasbx:login:token:";
	
	String SEQUENCE_CODE_PREFIX = "sequence:code:";
	
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
	
	/**
	 * 二维码
	 */
	String QR_CODE_LOGIN_QRID_PREFIX = "dasbx:qrcode:login:qrId:";
	/**
	 * 二维码绑定token
	 */
	String QR_CODE_BING_TOKEN_PREFIX = "dasbx:qrcode:bing:token:";
	
	String TEMP_DEVICE_BING_QRCODE_PREFIX = "dasbx:temp:device:bing:qrcode:";
	
	String TEMP_QRCODE_BING_TOKEN_PREFIX = "dasbx:temp:qrcode:bing:token:";
	
	String TEMP_TOKEN_PREFIX = "dasbx:temp:login:token:";
	
	//设备绑定邮箱绑定Code
	String DEVICE_BING_EMAIL_BING_CODE_PREFIX = "dasbx:device:bing:email:bing:code:";
}
