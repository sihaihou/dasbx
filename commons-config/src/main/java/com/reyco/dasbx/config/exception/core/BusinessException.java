package com.reyco.dasbx.config.exception.core;

/**
 * 业务异常
 * @author reyco
 *
 */
public class BusinessException extends DasbxException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5754208842697264226L;
 
	public BusinessException() {
		this("业务异常,请联系管理员...");
	}
	public BusinessException(String msg) {
		this(ExceptionCode.BUSINESS_EXCEPTION.getCode(),msg);
	}
	public BusinessException(Integer code, String msg) {
		this.type = ExceptionCode.BUSINESS_EXCEPTION.getType();
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
