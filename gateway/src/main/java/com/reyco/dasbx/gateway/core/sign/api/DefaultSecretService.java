package com.reyco.dasbx.gateway.core.sign.api;

import java.util.HashMap;

public class DefaultSecretService implements SecretService {

	private final static HashMap<String, String> secretMap;
	
	static {
		secretMap = new HashMap<String, String>();
		secretMap.put("key1", "111111");  
    	secretMap.put("key2", "222222"); 
    	secretMap.put("key3", "333333");  
    	secretMap.put("key4", "444444"); 
    	secretMap.put("key5", "555556");  
    	secretMap.put("key6", "666666"); 
	}
	
	@Override
	public String getSecret(String appkey) {
		return secretMap.get(appkey);
	}

	@Override
	public void saveSecret(String appkey, String secret) {
		secretMap.put(appkey, secret);
	}
	@Override
	public void clear() {
		secretMap.clear();
	}

}
