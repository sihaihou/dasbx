package com.reyco.dasbx.config.utils;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.commons.utils.net.RequestUtils;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.redis.RedisUtil;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.Constants;
import com.reyco.dasbx.model.vo.SysAccountToken;

/**
 * SessionUtils
 * @author reyco
 *
 */
public class TokenUtils {
	/**
	 * 获取Token
	 * @return
	 * @throws AuthenticationException 
	 */
	public static SysAccountToken getToken() throws AuthenticationException {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		return getToken(request);
	}
	/**
	 * 获取Token
	 * @param request
	 * @return
	 * @throws AuthenticationException 
	 */
	public static SysAccountToken getToken(HttpServletRequest request) throws AuthenticationException {
		String token = getTokenString(request);
		if(token==null) {
			throw new AuthenticationException("Not Login");
		}
		String deviceId = getDeviceId(request);
		if(deviceId==null) {
			throw new AuthenticationException("Not Login");
		}
		String deviceType = getDeviceType(request);
		if(deviceType==null) {
			throw new AuthenticationException("Not Login");
		}
		return getToken(deviceId,deviceType,token);
	}
	/**
	 * 获取Token
	 * @param deviceId 设备Id
	 * @param deviceId 设备类型
	 * @param token	   token
	 * @return
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
	/**
	 * 创建token
	 * @param deviceId 		设备ID
	 * @param deviceType  	设备类型：PC、mobile
	 * @param accountToken
	 */
	public static void createToken(SysAccountToken accountToken) {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String deviceId = getDeviceId(request);
		String deviceType = getDeviceType(request);
		createToken(deviceId, deviceType, accountToken);
	}
	/**
	 * 创建token
	 * @param deviceId 		设备ID
	 * @param deviceType  	设备类型：PC、mobile
	 * @param accountToken
	 */
	public static void createToken(String deviceId,String deviceType,SysAccountToken accountToken) {
		RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
		String newToken = accountToken.getToken();
		if(newToken==null) {
			HttpServletRequest request = RequestUtils.getHttpServletRequest();
			newToken = getCodeString(request);
			accountToken.setToken(newToken);
		}
		//用户与设备绑定
		redisUtil.add(CachePrefixConstants.USER_BING_DEVICE_PREFIX+accountToken.getUid(), deviceId);
		//设备与用户绑定
		redisUtil.add(CachePrefixConstants.DEVICE_BING_USER_PREFIX+deviceId, accountToken.getUid());
		//设备与token绑定
		redisUtil.add(CachePrefixConstants.DEVICE_BING_TOKEN_PREFIX+deviceId, newToken);
		if(deviceType.equalsIgnoreCase(Constants.DEVICE_PC_TYPE)) {
			//确保pc或mobile只有一台设备登录系统
			redisUtil.set(CachePrefixConstants.USER_LOGIN_UID+deviceType.toUpperCase()+":"+accountToken.getUid(),newToken,Constants.TOKEN_EXPIRES_TIME,TimeUnit.MILLISECONDS);
			//token
			redisUtil.set(CachePrefixConstants.TOKEN_PREFIX+newToken, JsonUtils.objToJson(accountToken),Constants.TOKEN_EXPIRES_TIME,TimeUnit.MILLISECONDS);
		}else if(deviceType.equalsIgnoreCase(Constants.DEVICE_MOBILE_TYPE)) {
			//确保pc或mobile只有一台设备登录系统
			redisUtil.set(CachePrefixConstants.USER_LOGIN_UID+deviceType.toUpperCase()+":"+accountToken.getUid(),newToken,Constants.NOT_EXPIRES_TIME,TimeUnit.MILLISECONDS);
			//token
			redisUtil.set(CachePrefixConstants.TOKEN_PREFIX+newToken, JsonUtils.objToJson(accountToken),Constants.NOT_EXPIRES_TIME,TimeUnit.MILLISECONDS);
		}
	}
	/**
	 * 获取临时token
	 * @param accountToken
	 */
	public static SysAccountToken getTempToken(String qrcode,String token) {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String deviceId = getDeviceId(request);
		RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
		String deviceIdKey = CachePrefixConstants.TEMP_DEVICE_BING_QRCODE_PREFIX+deviceId;
		String qrcodeKey = CachePrefixConstants.TEMP_QRCODE_BING_TOKEN_PREFIX+qrcode;
		String tokenKey = CachePrefixConstants.TEMP_TOKEN_PREFIX+token;
		if(redisUtil.hasKey(deviceIdKey)
				&& redisUtil.get(deviceIdKey).equals(qrcode)
				&& redisUtil.hasKey(qrcodeKey)
				&& redisUtil.get(qrcodeKey).equals(token)
				&& redisUtil.hasKey(tokenKey)) {
			String sysAccountTokenJson = redisUtil.get(tokenKey);
			SysAccountToken sysAccountToken = JsonUtils.jsonToObj(sysAccountTokenJson, SysAccountToken.class);
			return sysAccountToken;
		}
		return null;
	}
	/**
	 * 创建临时token
	 * @param accountToken
	 */
	public static void createTempToken(String qrcode,SysAccountToken accountToken) {
		String newToken = accountToken.getToken();
		if(newToken==null) {
			HttpServletRequest request = RequestUtils.getHttpServletRequest();
			newToken = getCodeString(request);
			accountToken.setToken(newToken);
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String deviceId = getDeviceId(request);
		RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
		redisUtil.set(CachePrefixConstants.TEMP_DEVICE_BING_QRCODE_PREFIX+deviceId, qrcode,Constants.TEMP_TOKEN_EXPIRES_TIME,TimeUnit.MILLISECONDS);
		redisUtil.set(CachePrefixConstants.TEMP_QRCODE_BING_TOKEN_PREFIX+qrcode, accountToken.getToken(),Constants.TEMP_TOKEN_EXPIRES_TIME,TimeUnit.MILLISECONDS);
		redisUtil.set(CachePrefixConstants.TEMP_TOKEN_PREFIX+accountToken.getToken(), JsonUtils.objToJson(accountToken),Constants.TEMP_TOKEN_EXPIRES_TIME,TimeUnit.MILLISECONDS);
	}
	/**
	 * 删除临时token
	 * @param accountToken
	 */
	public static void removeTempToken(String qrcode,String token) {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		String deviceId = getDeviceId(request);
		RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
		redisUtil.delete(CachePrefixConstants.TEMP_DEVICE_BING_QRCODE_PREFIX+deviceId);
		redisUtil.delete(CachePrefixConstants.TEMP_QRCODE_BING_TOKEN_PREFIX+qrcode);
		redisUtil.delete(CachePrefixConstants.TEMP_TOKEN_PREFIX+token);
	}
	/**
	 * 移除token
	 * @return
	 */
	public static void removeToken() {
		HttpServletRequest request = RequestUtils.getHttpServletRequest();
		removeToken(request);
	}
	/**
	 * 移除token
	 * @param request
	 * @return
	 */
	public static void removeToken(HttpServletRequest request) {
		String token = getTokenString(request);
		String deviceId = getDeviceId(request);
		String deviceType = getDeviceType(request);
		removeToken(deviceId,deviceType,token);
	}
	/**
	 * 删除Token
	 * @param deviceId
	 * @param deviceType
	 * @param tokenKey
	 */
	public static void removeToken(String deviceId,String deviceType,String token) {
		RedisUtil redisUtil = SpringContextUtils.getBean(RedisUtil.class);
		String tokenKey = CachePrefixConstants.TOKEN_PREFIX+token;
		if(!redisUtil.hasKey(tokenKey)) {
			return;
		}
		String tokenJson = redisUtil.get(tokenKey);
		SysAccountToken sysAccountToken = JsonUtils.jsonToObj(tokenJson, SysAccountToken.class);
		if(redisUtil.isMember(CachePrefixConstants.USER_BING_DEVICE_PREFIX+sysAccountToken.getUid(),deviceId) 
				&& redisUtil.isMember(CachePrefixConstants.DEVICE_BING_USER_PREFIX+deviceId,sysAccountToken.getUid())
				&& redisUtil.isMember(CachePrefixConstants.DEVICE_BING_TOKEN_PREFIX+deviceId,token)) {
			redisUtil.remove(CachePrefixConstants.DEVICE_BING_TOKEN_PREFIX+deviceId, token);
			redisUtil.delete(CachePrefixConstants.USER_LOGIN_UID+deviceType.toUpperCase()+":"+sysAccountToken.getUid());
			redisUtil.delete(tokenKey);
		}
	}
	
	/**
	 * 获取Token
	 * @param request
	 * @return
	 */
	public static String getTokenString(HttpServletRequest request) {
		return getOption(request, Constants.TOKEN_NAME);
	}
	/**
	 * 获取Code
	 * @param request
	 * @return
	 */
	public static String getCodeString(HttpServletRequest request) {
		return getOption(request, Constants.CODE_NAME);
	}
	/**
	 * 获取设备deviceId
	 * @param request
	 * @return
	 */
	public static String getDeviceId(HttpServletRequest request) {
		return getOption(request, Constants.DEVICE_ID_NAME);
	}
	/**
	 * 获取设备deviceId
	 * @param request
	 * @return
	 */
	public static String getDeviceType(HttpServletRequest request) {
		return getOption(request, Constants.DEVICE_TYPE_NAME);
	}
	/**
	 * 获取option
	 * @param request
	 * @param optionName
	 * @return
	 */
	private static String getOption(HttpServletRequest request,String optionName) {
		String option = RequestUtils.getHeaderOptional(request, optionName, null);
		if(option==null) {
			option = RequestUtils.getCookieOptional(request, optionName, null);
		}
		return option;
	}
}
