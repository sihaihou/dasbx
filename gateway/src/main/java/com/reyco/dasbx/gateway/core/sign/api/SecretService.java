package com.reyco.dasbx.gateway.core.sign.api;

public interface SecretService {
	
	String getSecret(String appkey);
	
	void saveSecret(String appkey,String secret);
	
	void clear();
}
