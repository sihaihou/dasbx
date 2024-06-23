package com.reyco.dasbx.gateway.core.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.gateway.core.filter.SecurityGlobalFilter;
import com.reyco.dasbx.gateway.core.filter.properties.Filter;
import com.reyco.dasbx.gateway.core.filter.properties.FilterProperties;
import com.reyco.dasbx.gateway.core.filter.properties.Service;

@Configuration
@ConditionalOnBean(FilterProperties.class)
public class SecurityFilterConfig {
	@Autowired
	private FilterProperties filterProperties;
	@Bean
	public SecurityGlobalFilter securityGlobalFilter() {
		SecurityGlobalFilter securityGlobalFilter = new SecurityGlobalFilter();
		HashMap<String, Set<String>> excludeMap = new HashMap<String,Set<String>>();
		Map<String, Filter> filters = filterProperties.getFilters();
		Filter filter = filters.get(SecurityGlobalFilter.SECURITY_FILTER_NAME);
		if(filter==null) {
			filter = new Filter();
		}
		HashMap<String, Service> services = filter.getServices();
		for (Entry<String,Service> serviceEntry : services.entrySet()) {
			String serviceName = serviceEntry.getKey();
			Service service = serviceEntry.getValue();
			String servicePrefix = serviceName;
			if(serviceName.contains("-")) {
				servicePrefix = serviceName.substring(0,serviceName.indexOf("-"));
			}
			excludeMap.put(servicePrefix, service.getExcludes());
		}
		securityGlobalFilter.setOrder(filter.getOrder());
		securityGlobalFilter.setExcludeMap(excludeMap);
		return securityGlobalFilter;
	}
	//@Bean
	public SecurityGlobalFilter securityGlobalFilter1() {
		SecurityGlobalFilter securityGlobalFilter = new SecurityGlobalFilter();
		HashMap<String, Set<String>> excludeMap = new HashMap<String,Set<String>>();
		Set<String> userExcludeSet = new HashSet<>();
		userExcludeSet.add("/user/test/**");
		userExcludeSet.add("/user/sys/account/getByUid");
		excludeMap.put("user", userExcludeSet);
		
		Set<String> loginExcludeSet = new HashSet<>();
		loginExcludeSet.add("/login/sys/code");
		loginExcludeSet.add("/login/sys/getQRCode");
		loginExcludeSet.add("/login/sys/getToken");
		loginExcludeSet.add("/login/sys/login");
		loginExcludeSet.add("/login/sys/createEmailCode");
		loginExcludeSet.add("/login/sys/emailLogin");
		loginExcludeSet.add("/login/login/**");
		loginExcludeSet.add("/login/oauth2/**");
		excludeMap.put("login", loginExcludeSet);
		
		Set<String> openExcludeSet = new HashSet<>();
		openExcludeSet.add("/open/application/getByClientId");
		excludeMap.put("open", userExcludeSet);
		
		Set<String> portalExcludeSet = new HashSet<>();
		excludeMap.put("portal", portalExcludeSet);
		
		securityGlobalFilter.setExcludeMap(excludeMap);
		return securityGlobalFilter;
	}
}
