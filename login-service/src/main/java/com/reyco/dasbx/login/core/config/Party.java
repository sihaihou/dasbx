package com.reyco.dasbx.login.core.config;

public class Party {
	private String clientId;
	private String clientSecret;
	private String authorizeUrl;
	private String callbackUrl;
	private String tokenUrl;
	private String infoUrl;
	private String selfSuccessUrl;
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
	public String getAuthorizeUrl() {
		return authorizeUrl;
	}
	public void setAuthorizeUrl(String authorizeUrl) {
		this.authorizeUrl = authorizeUrl;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getTokenUrl() {
		return tokenUrl;
	}
	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}
	public String getInfoUrl() {
		return infoUrl;
	}
	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}
	public String getSelfSuccessUrl() {
		return selfSuccessUrl;
	}
	public void setSelfSuccessUrl(String selfSuccessUrl) {
		this.selfSuccessUrl = selfSuccessUrl;
	}
}
