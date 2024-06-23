package com.reyco.dasbx.oss.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value=OssProperties.OSS_PREFIX)
public class OssProperties {
	
	public final static String OSS_PREFIX = "reyco.dasbx.oss";
	
	private String basePath;
	private String baseUrl;
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
