package com.reyco.dasbx.login.core.model.domain;

import com.reyco.dasbx.model.Base;

public class Application extends Base{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2850961479413180051L;
	private String name;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private Byte status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	
}
