package com.reyco.dasbx.resource.core.interceptor;

public class DefaultResourceAttribute implements ResourceAttribute {
	private String name;
	private String value;
	private String resource;
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}

}
