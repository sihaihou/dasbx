package com.reyco.dasbx.gateway.core.exception;

public class AuthenticationException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -838354297539013905L;
	private String msg;
	public AuthenticationException(String msg) {
		this.msg = msg;
	}
	@Override
	public String getMessage() {
		return this.msg;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
