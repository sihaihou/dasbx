package com.reyco.dasbx.gateway.core.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.gateway.core.filter.AuthGlobalFilter;
import com.reyco.dasbx.gateway.core.filter.properties.Filter;
import com.reyco.dasbx.gateway.core.filter.properties.FilterProperties;
import com.reyco.dasbx.gateway.core.filter.properties.Service;

@Configuration
@ConditionalOnBean(FilterProperties.class)
public class AuthFilterConfig {
	@Autowired
	private FilterProperties filterProperties;
	@Bean
	public AuthGlobalFilter authGlobalFilter() {
		AuthGlobalFilter authGlobalFilter = new AuthGlobalFilter();
		HashMap<String, Set<String>> excludeMap = new HashMap<String,Set<String>>();
		Map<String, Filter> filters = filterProperties.getFilters();
		Filter filter = filters.get(AuthGlobalFilter.AUTH_FILTER_NAME);
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
		authGlobalFilter.setOrder(filter.getOrder());
		authGlobalFilter.setExcludeMap(excludeMap);
		return authGlobalFilter;
	}
	//@Bean
	public AuthGlobalFilter authGlobalFilter1() {
		AuthGlobalFilter authGlobalFilter = new AuthGlobalFilter();
		HashMap<String, Set<String>> excludeMap = new HashMap<String,Set<String>>();
		Set<String> userExcludeSet = new HashSet<>();
		userExcludeSet.add("/user/test/**");
		userExcludeSet.add("/user/fullname/**");
		userExcludeSet.add("/user/sys/account/register");
		userExcludeSet.add("/user/sys/account/getByUid");
		userExcludeSet.add("/user/sys/account/getByUsername");
		userExcludeSet.add("/user/sys/account/getByEmail");
		userExcludeSet.add("/user/sys/account/*");
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
		openExcludeSet.add("/open/application/**");
		excludeMap.put("open", openExcludeSet);
		
		Set<String> portalExcludeSet = new HashSet<>();
		portalExcludeSet.add("/portal/**");
		excludeMap.put("portal", portalExcludeSet);
		
		authGlobalFilter.setExcludeMap(excludeMap);
		return authGlobalFilter;
	}
}
