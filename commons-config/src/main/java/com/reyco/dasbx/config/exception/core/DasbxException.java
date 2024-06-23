package com.reyco.dasbx.config.exception.core;

/**
 * 异常基础类
 * @author reyco
 *
 */
public class DasbxException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2775136071227808866L;
	protected String type;
	protected Integer code;
	protected String msg;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
