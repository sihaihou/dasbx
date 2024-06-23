package com.reyco.dasbx.login.core.service.authentication;

import com.reyco.dasbx.config.exception.core.ArgumentException;
import com.reyco.dasbx.config.exception.core.AuthenticationException;

/**
 * 抽象认证器
 * @author reyco
 *
 */
public abstract class AbstractAuthenticator implements Authenticator{

	@Override
	public AuthenticationInfo authenticate(AuthenticationToken token) throws ArgumentException, AuthenticationException {
		if (token == null) {
			throw new ArgumentException("Method argumet (authentication token) cannot be null.");
		}
		AuthenticationInfo info;
		try {
			 info = doAuthenticate(token);
			 if(info==null) {
				 String msg = "认证失败，请确认您的信息是否正确！";
				 throw new AuthenticationException(msg);
			 }
		} catch (Throwable t) {
			AuthenticationException ae = null;
			if(t instanceof AuthenticationException) {
				ae = (AuthenticationException)t;
			}
			if(ae==null) {
				String msg = "Authentication failed for token submission " + token + "].  Possible unexpected error? (Typical or expected login exceptions should extend from AuthenticationException)";
				ae = new AuthenticationException(msg);
			}
			throw ae;
		}
		return info;
	}
	protected abstract AuthenticationInfo doAuthenticate(AuthenticationToken token) throws AuthenticationException;
	
}
