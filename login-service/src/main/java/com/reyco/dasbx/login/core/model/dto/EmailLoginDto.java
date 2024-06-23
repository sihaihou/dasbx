package com.reyco.dasbx.login.core.model.dto;

/**
 * 邮箱登录Dto
 * @author reyco
 *
 */
public class EmailLoginDto extends LoginDto{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6927074870271774900L;
	private String email;
	private String code;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
