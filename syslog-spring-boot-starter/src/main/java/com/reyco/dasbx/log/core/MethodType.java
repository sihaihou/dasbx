package com.reyco.dasbx.log.core;

public enum MethodType {
	GET("GET"),
	HEAD("HEAD"),
	POST("POST"),
	PUT("PUT"),
	PATCH("PATCH"),
	DELETE("DELETE"),
	OPTIONS("OPTIONS"),
	TRACE("TRACE");
	private String method;
	
	MethodType(String method) {
		this.method = method;
	}
	public static MethodType getMethodType(String method) {
		for (MethodType methodType : MethodType.values()) {
			if (methodType.method == method) {
				return methodType;
			}
		}
		return null;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
}
