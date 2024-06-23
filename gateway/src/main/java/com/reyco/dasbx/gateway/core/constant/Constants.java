package com.reyco.dasbx.gateway.core.constant;

public interface Constants {
	//静态资源
	String STATIC_RESOURCE_PATTERN = "\\.(html|css|js|map|png|jpg|jpeg|gif|woff2|woff)$";
	//签名相关
	String DASBX_KEY = "Dasbx-Key";
	
	String DASBX_NONCE = "Dasbx-Nonce";
	
	String DASBX_TIMESTAMP = "Dasbx-Timestamp";
	
	String DASBX_CONTENT_SIGNATURE = "Dasbx-Content-Signature";
	
	String DASBX_SIGNATURE = "Dasbx-Signature";
	
	
	String CODE_NAME = "dasbx-code";
	
	String TOKEN_NAME = "dasbx-token";
	
	String ACCEPT_NAME = "dasbx-accept";
	
	String DEVICE_ID_NAME = "dasbx-deviceId";
	
	String DEVICE_TYPE_NAME = "dasbx-deviceType";
	
	String DEVICE_PC_TYPE = "PC";
	
	String DEVICE_MOBILE_TYPE = "mobile";
	
	Long TOKEN_EXPIRES_TIME = 1000*60*30L;
	
	
}
