package com.reyco.dasbx.gateway.core.filter.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(FilterProperties.FILTER_EXCLUDE_PREFIX)
public class FilterProperties {
	public final static String FILTER_EXCLUDE_PREFIX = "reyco.dasbx";
	/**
	 * key: auth-filter、security-filter、sign-filter...
	 */
	private Map<String,Filter> filters = new HashMap<String,Filter>();

	public Map<String, Filter> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Filter> filters) {
		this.filters = filters;
	}
	
}
