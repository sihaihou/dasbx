package com.reyco.dasbx.rate.limit.exception;

import com.reyco.dasbx.commons.exception.DasbxException;
import com.reyco.dasbx.commons.exception.ExceptionCode;


public class RateLimitException extends DasbxException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4194912835390927183L;
 
	public RateLimitException() {
		this("请求过于频繁，请稍后再试!");
	}
	public RateLimitException(String msg) {
		this(ExceptionCode.RATE_LIMIT_EXCEPTION.getCode(),msg);
	}
	public RateLimitException(Integer code, String msg) {
		this.type = ExceptionCode.RATE_LIMIT_EXCEPTION.getType();
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
