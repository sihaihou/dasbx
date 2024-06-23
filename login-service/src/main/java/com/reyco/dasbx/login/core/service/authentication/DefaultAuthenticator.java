package com.reyco.dasbx.login.core.service.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.config.exception.core.AuthenticationException;

/**
 * 默认的认证器实现
 * @author reyco
 *
 */
@Component
public class DefaultAuthenticator extends AbstractAuthenticator{
	
	@Autowired
	private Authenticating authenticating;
	
	@Override
	public AuthenticationInfo doAuthenticate(AuthenticationToken token) throws AuthenticationException {
		return authenticating.getAuthenticationInfo(token);
	}

}
