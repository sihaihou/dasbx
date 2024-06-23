package com.reyco.dasbx.user.core.model.vo;

import com.reyco.dasbx.commons.utils.ToString;

public class AccountLoginVO extends ToString{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5741995894677587573L;
	private String username;
	private String token;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
