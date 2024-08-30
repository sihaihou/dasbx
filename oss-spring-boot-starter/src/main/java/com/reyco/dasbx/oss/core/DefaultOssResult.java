package com.reyco.dasbx.oss.core;

public class DefaultOssResult implements OssResult{
	private Type type;
	protected String url;
	@Override
	public Type getType() {
		return this.type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	@Override
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
