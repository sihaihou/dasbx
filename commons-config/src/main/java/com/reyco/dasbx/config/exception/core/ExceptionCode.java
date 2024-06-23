package com.reyco.dasbx.config.exception.core;
import com.reyco.dasbx.commons.domain.R;
/**
 * 错误代码
 * @author reyco
 *
 */
public enum ExceptionCode {
	SYSTEM_EXCEPTION("System",210,"系统异常"),
	AUTHENTICATION_EXCEPTION("Authentication",R.AUTH_FAIL_CODE,"认证异常"),
	BUSINESS_EXCEPTION("Business",212, "业务异常"),
	ARGUMENT_EXCEPTION("Argument",R.PARAM_ERROR_CODE, "参数异常");
	private String type;
	private Integer code;
	private String desc;

	ExceptionCode(String type,Integer code, String desc) {
		this.type = type;
		this.code = code;
		this.desc = desc;
	}
	public static ExceptionCode getExceptionCode(String type) {
		for (ExceptionCode exceptionCode : ExceptionCode.values()) {
			if (exceptionCode.type == type) {
				return exceptionCode;
			}
		}
		return null;
	}
	public static ExceptionCode getExceptionCode(Integer code) {
		for (ExceptionCode exceptionCode : ExceptionCode.values()) {
			if (exceptionCode.code == code) {
				return exceptionCode;
			}
		}
		return null;
	}
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
