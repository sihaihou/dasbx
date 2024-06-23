package com.reyco.dasbx.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.config.web.interceptor.properteies.Interceptor;

@Component
@RefreshScope
@ConfigurationProperties(DasbxConfig.DASBX_PREFIX)
public class DasbxConfig {
	
	public final static String DASBX_PREFIX = "reyco.dasbx";
	
	private String basePath;
	private String baseUrl;
	private Map<String,Interceptor> interceptors = new HashMap<>();
	private Map<String,Object> constants = new HashMap<>();
	
	public String getBasePath(){
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
	public Map<String, Interceptor> getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(Map<String, Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
	public Map<String, Object> getConstants() {
		return constants;
	}
	public void setConstants(Map<String, Object> constants) {
		this.constants = constants;
	}
}
