package com.reyco.dasbx.user.core.model.dto;

import com.reyco.dasbx.commons.utils.serializable.ToString;

/**
 * 登录参数对象
 * @author reyco
 *
 */
public class AccountLoginDto extends ToString {
	/**
	 * 
	 */
	private static final long serialVersionUID = -129981006171799449L;
	private String username;
	private String password;
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
}
