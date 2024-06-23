package com.reyco.dasbx.login.core.service.authentication;

public interface AuthenticationInfo {
	
	/**
	 * 用户信息
	 * @return
	 */
	Object getPrincipal();
	
	/**
	 * 凭证
	 * @return
	 */
	Object getCredentials();

}
