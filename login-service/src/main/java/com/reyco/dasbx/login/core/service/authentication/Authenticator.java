package com.reyco.dasbx.login.core.service.authentication;

import com.reyco.dasbx.commons.exception.ArgumentException;
import com.reyco.dasbx.commons.exception.AuthenticationException;

/**
 * 认证器
 * @author reyco
 *
 */
public interface Authenticator {
	
	AuthenticationInfo authenticate(AuthenticationToken token) throws ArgumentException,AuthenticationException;
	
}
