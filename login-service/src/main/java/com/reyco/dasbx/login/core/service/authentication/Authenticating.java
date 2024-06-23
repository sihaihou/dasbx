package com.reyco.dasbx.login.core.service.authentication;

import com.reyco.dasbx.config.exception.core.AuthenticationException;

/**
 * 正在认证
 * @author reyco
 *
 */
public interface Authenticating {
	
	/**
	 * 获取认证信息
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException;

}
