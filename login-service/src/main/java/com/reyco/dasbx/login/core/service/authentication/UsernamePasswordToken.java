package com.reyco.dasbx.login.core.service.authentication;

public class UsernamePasswordToken implements AuthenticationToken {
	private String username;
	private String password;

	public UsernamePasswordToken() {
	}

	public UsernamePasswordToken(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public Object getPrincipal() {
		return getUsername();
	}

	@Override
	public Object getCredentials() {
		return getPassword();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append(" - ");
		sb.append(username);
		return sb.toString();
	}
}
