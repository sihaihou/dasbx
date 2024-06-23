package com.reyco.dasbx.login.core.service.oauth2;

import com.alibaba.fastjson.JSONObject;

public interface Oauth2Service {
	
	boolean authorize(String clientId,String redirectUri,String responseType) throws Exception;
	
	JSONObject createToken(String t) throws Exception;
	
	JSONObject accessToken(String clientId, String clientSecret, String redirectUri, String code,String grantType) throws Exception;
	
	JSONObject getUserInfo(String accessToken) throws Exception;
}
