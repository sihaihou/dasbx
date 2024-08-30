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
	
	
	/**
	 * 同步ES初始化阶段
	 */
	String SYNC_ES_INIT_PREFIX = "sync:es:init:";
	/**
	 * 视频同步ES重试
	 */
	String SYNC_ES_VIDEO_RETRY = "sync:es:retry:video";
	/**
	 * 账号同步ES重试
	 */
	String SYNC_ES_ACCOUNT_RETRY = "sync:es:retry:account";
	/**
	 * 角色同步ES重试
	 */
	String SYNC_ES_ROLE_RETRY = "sync:es:retry:role";
	
	
	/**
	 * 系统日志重试
	 */
	String SYS_LOG_RETRY = "sys:log:retry";
	/**
	 * 登录日志重试
	 */
	String LOGIN_LOG_RETRY = "login:log:retry";
	/**
	 * 登出日志重试
	 */
	String LOGOUT_LOG_RETRY = "logout:log:retry";
	/**
	 * 视频解码重试
	 */
	String VIDEO_DECODE_RETRY = "video:decode:retry";
	/**
	 * 账号注册ES重试
	 */
	String ACCOUNT_REGISTER_RETRY = "account:register:retry";
	
	/**
	 * rabbit 序列id 防重复
	 */
	String RABBIT_CORRELATIONDATA_ID_TYPE_PREFIX = "rabbit:correlationdata:id:type:";
	
	
}
