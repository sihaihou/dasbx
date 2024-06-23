package com.reyco.dasbx.model.constants;

public interface Constants {
	
	Long SUPER_ADMIN = 1L;
	
	String CODE_API = "/sys/code";
	
	String LOG_API = "/sys/log/**";
	
	String ACCEPT_NAME = "dasbx-accept";
	
	String DEVICE_ID_NAME = "dasbx-deviceId";
	
	String DEVICE_TYPE_NAME = "dasbx-deviceType";
	
	String DEVICE_PC_TYPE = "PC";
	
	String DEVICE_MOBILE_TYPE = "mobile";
	
	String CODE_NAME = "dasbx-code";
	
	String TOKEN_NAME = "dasbx-token";
	
	Long TOKEN_EXPIRES_TIME = 1000*60*30L;
	
	Long NOT_EXPIRES_TIME = 1000*60*60*24*365L;
	
	Long TEMP_TOKEN_EXPIRES_TIME = 1000*60*1L;
	
	Integer DEFAULT_PAGE_SIZE = 10;
	
	Long VERIFICATION_CODE_TIME = 1000*60*5L;
	
}
