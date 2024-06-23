package com.reyco.dasbx.login.core.service.authentication;

/**
 * token
 * @author reyco
 *
 */
public interface AuthenticationToken {

	Object getPrincipal();

	Object getCredentials();
}
