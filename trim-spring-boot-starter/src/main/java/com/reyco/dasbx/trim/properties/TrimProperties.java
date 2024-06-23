package com.reyco.dasbx.trim.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

@ConfigurationProperties(prefix=TrimProperties.REMOVESPACE_PREFIX)
public class TrimProperties {
	
	public final static String REMOVESPACE_PREFIX = "reyco.dasbx.trim";
	
	private String urlPatterns = "/*";
	
	private int order = Ordered.LOWEST_PRECEDENCE - 2;
	
	public String getUrlPatterns() {
		return urlPatterns;
	}
	public void setUrlPatterns(String urlPatterns) {
		this.urlPatterns = urlPatterns;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
