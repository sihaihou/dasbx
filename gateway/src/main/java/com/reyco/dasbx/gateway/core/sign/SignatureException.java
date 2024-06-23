package com.reyco.dasbx.gateway.core.sign;

/**
 * 签名异常
 * @author reyco
 *
 */
public class SignatureException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7810933757114315340L;
	private String message;
	public SignatureException() {
		this("签名异常...");
	}
	public SignatureException(String message) {
		super();
		this.message = message;
	}
	@Override
	public String getMessage() {
		return message;
	}
}
