package com.reyco.dasbx.login.core.service.oauth2.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.config.exception.core.ExceptionCode;
import com.reyco.dasbx.login.core.feign.ApplicationFeignClientService;
import com.reyco.dasbx.login.core.model.domain.Application;
import com.reyco.dasbx.login.core.service.LoginService;
import com.reyco.dasbx.login.core.service.oauth2.Oauth2Service;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.vo.SysAccountToken;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;

@Service
public class Oauth2ServiceImpl implements Oauth2Service{
	
	@Autowired
	private ApplicationFeignClientService applicationFeignClientService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public boolean authorize(String clientId,String redirectUri,String responseType)  throws Exception{
		if (StringUtils.isBlank(clientId) || StringUtils.isBlank(redirectUri) || StringUtils.isBlank(responseType)) {
			throw new RuntimeException("参数错误");
		}
		Application application = applicationFeignClientService.getByClientId(clientId);
		if(application==null) {
			throw new RuntimeException("应用不存在");
		}
		if (!application.getRedirectUri().equals(redirectUri)) {
			throw new RuntimeException("回调地址错误");
		}
		if (!responseType.equals("code")) {
			throw new RuntimeException("response_type错误");
		}
		return true;
	}

	@Override
	public JSONObject createToken(String t) throws Exception {
		SysAccountToken accountToken = loginService.login(t);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("token", accountToken.getToken());
		return jsonObject;
	}

	@Override
	public JSONObject accessToken(String clientId, String clientSecret, String redirectUri, String code,
			String grantType) throws Exception {
		if (StringUtils.isBlank(clientId) || StringUtils.isBlank(clientSecret) || 
				StringUtils.isBlank(redirectUri) || StringUtils.isBlank(code) || StringUtils.isBlank(grantType)) {
			throw new RuntimeException(ExceptionCode.ARGUMENT_EXCEPTION.getDesc());
		}
		Application application = applicationFeignClientService.getByClientId(clientId);
		if(application==null) {
			throw new RuntimeException("应用不存在");
		}
		if (!application.getClientSecret().equals(clientSecret) || !application.getRedirectUri().equals(redirectUri)
				|| !"authorization_code".equals(grantType)) {
			throw new RuntimeException(ExceptionCode.ARGUMENT_EXCEPTION.getDesc());
		}
		String key = CachePrefixConstants.TOKEN_PREFIX+code;
		if(!redisUtil.hasKey(key)) {
			throw new RuntimeException("Code invalid");
		}
		String accountTokenJson = redisUtil.get(key);
		SysAccountToken accountToken = JsonUtils.jsonToObj(accountTokenJson, SysAccountToken.class);
		long expiresIn = redisUtil.getExpire(CachePrefixConstants.TOKEN_PREFIX+code);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("access_token", accountToken.getToken());
		jsonObject.put("expires_in", "" + expiresIn);
		jsonObject.put("remind_in", "1234");
		jsonObject.put("uid", accountToken.getUid());
		return jsonObject;
	}

	@Override
	public JSONObject getUserInfo(String accessToken) throws Exception {
		try {
			if (StringUtils.isBlank(accessToken)) {
				throw new RuntimeException(ExceptionCode.ARGUMENT_EXCEPTION.getDesc());
			}
			String token = CachePrefixConstants.TOKEN_PREFIX+accessToken;
			String accountTokenJson = redisUtil.get(token);
			SysAccountToken accountToken = JsonUtils.jsonToObj(accountTokenJson, SysAccountToken.class);
			long expiresIn = redisUtil.getExpire(token);
			if (expiresIn<0) {
				throw new RuntimeException("accessToken invalid");
			}
			JSONObject userInfoObj = new JSONObject();
			userInfoObj.put("nickname", accountToken.getNickname());
			userInfoObj.put("scope", "");
			userInfoObj.put("create_at", accountToken.getGmtCreate());
			userInfoObj.put("uid", accountToken.getUid());
			return userInfoObj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("accessToken invalid");
	}

}
