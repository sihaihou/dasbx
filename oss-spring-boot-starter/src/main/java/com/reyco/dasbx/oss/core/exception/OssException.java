package com.reyco.dasbx.oss.core.exception;

public class OssException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8481544723880410866L;
	public OssException() {
		this("OSS encountered an unknown exception.");
	}
	public OssException(String msg) {
		super(msg);
	}
}
