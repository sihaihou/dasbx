package com.reyco.dasbx.config.web.interceptor.properteies;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(InterceptorProperties.INTERCEPTOR_EXCLUDE_PREFIX)
public class InterceptorProperties {
	
	public final static String INTERCEPTOR_EXCLUDE_PREFIX = "reyco.dasbx";
	/**
	 * key: auth、sign、security
	 */
	private Map<String,Interceptor> interceptors = new HashMap<>();
	public Map<String, Interceptor> getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(Map<String, Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
}
