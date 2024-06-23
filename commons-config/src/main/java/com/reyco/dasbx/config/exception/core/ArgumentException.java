package com.reyco.dasbx.config.exception.core;

/**
 * 参数异常
 * @author reyco
 *
 */
public class ArgumentException extends DasbxException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4194912835390927183L;
 
	public ArgumentException() {
		this("参数异常,请联系管理员...");
	}
	public ArgumentException(String msg) {
		this(ExceptionCode.ARGUMENT_EXCEPTION.getCode(),msg);
	}
	public ArgumentException(Integer code, String msg) {
		this.type = ExceptionCode.ARGUMENT_EXCEPTION.getType();
		this.code = code;
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
