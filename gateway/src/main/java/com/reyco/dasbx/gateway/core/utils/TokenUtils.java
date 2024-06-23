package com.reyco.dasbx.gateway.core.utils;

import java.util.concurrent.TimeUnit;

import com.reyco.dasbx.gateway.core.config.redis.RedisUtil;
import com.reyco.dasbx.gateway.core.constant.CachePrefixConstants;
import com.reyco.dasbx.gateway.core.constant.Constants;
import com.reyco.dasbx.gateway.core.exception.AuthenticationException;
import com.reyco.dasbx.gateway.core.model.SysAccountToken;

public class TokenUtils {
	/**
	 * 获取Token
	 * @param deviceId 设备Id
	 * @param deviceId 设备类型
	 * @param token	   token
	 * @return
	 * @throws RuntimeException 
	 * @throws AuthenticationException 
	 */
	public static SysAccountToken getToken(String deviceId,String deviceType,String token) throws AuthenticationException {
		RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
		if(!redisUtil.isMember(CachePrefixConstants.DEVICE_BING_TOKEN_PREFIX+deviceId, token)) {
			throw new AuthenticationException("Not Login");
		}
		String tokenKey = CachePrefixConstants.TOKEN_PREFIX+token;
		String tokenJson = redisUtil.get(tokenKey);
		SysAccountToken sysAccountToken = JsonUtils.jsonToObj(tokenJson, SysAccountToken.class);
		if(deviceType.equalsIgnoreCase(Constants.DEVICE_PC_TYPE)
				&& !redisUtil.hasKey(CachePrefixConstants.USER_LOGIN_UID+deviceType.toUpperCase()+":"+sysAccountToken.getUid())) {
			redisUtil.remove(CachePrefixConstants.DEVICE_BING_TOKEN_PREFIX+deviceId, token);
			redisUtil.delete(tokenKey);
			throw new AuthenticationException("Token expires");
		}
		if(deviceType.equalsIgnoreCase(Constants.DEVICE_MOBILE_TYPE) || deviceType.equalsIgnoreCase(Constants.DEVICE_PC_TYPE)) {
			redisUtil.set(CachePrefixConstants.USER_LOGIN_UID+deviceType.toUpperCase()+":"+sysAccountToken.getUid(),token,Constants.TOKEN_EXPIRES_TIME,TimeUnit.MILLISECONDS);
		}
		return sysAccountToken;
	}
}
