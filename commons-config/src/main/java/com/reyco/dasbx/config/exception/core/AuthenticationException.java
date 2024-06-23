package com.reyco.dasbx.config.exception.core;

/**
 * 认证异常
 * @author reyco
 *
 */
public class AuthenticationException extends DasbxException  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7719078040486519842L;
	public AuthenticationException() {
		this("认证失败...");
	}
	public AuthenticationException(String msg) {
		this(ExceptionCode.AUTHENTICATION_EXCEPTION.getCode(),msg);
	}
	public AuthenticationException(Integer code, String msg) {
		this.type = ExceptionCode.AUTHENTICATION_EXCEPTION.getType();
		this.code = code;
		this.msg = msg;
	}
	@Override
	public String getMessage() {
		return "认证失败,code:" + getCode() + ",msg:" + getMsg();
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
