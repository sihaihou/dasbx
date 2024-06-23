package com.reyco.dasbx.login.core.service.authentication;

/**
 * 
 * @author reyco
 *
 */
public class SimpleAuthenticationInfo implements AuthenticationInfo {
	
	Object principal;
	
	Object credentials;
	
	public SimpleAuthenticationInfo() {
	}
	public SimpleAuthenticationInfo(Object credentials) {
		super();
		this.credentials = credentials;
	}
	public SimpleAuthenticationInfo(Object principal,Object credentials) {
		super();
		this.principal = principal;
		this.credentials = credentials;
	}
	@Override
	public Object getCredentials() {
		return credentials;
	}
	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
	@Override
	public Object getPrincipal() {
		return principal;
	}
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
}
