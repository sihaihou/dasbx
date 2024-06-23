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

import com.reyco.dasbx.gateway.core.filter.SignGlobalFilter;
import com.reyco.dasbx.gateway.core.filter.properties.Filter;
import com.reyco.dasbx.gateway.core.filter.properties.FilterProperties;
import com.reyco.dasbx.gateway.core.filter.properties.Service;
import com.reyco.dasbx.gateway.core.sign.DefaultSignature;
import com.reyco.dasbx.gateway.core.sign.Signature;
import com.reyco.dasbx.gateway.core.sign.api.ApiSignature;
import com.reyco.dasbx.gateway.core.sign.api.DefaultApiSignature;
import com.reyco.dasbx.gateway.core.sign.api.SecretService;

@Configuration
@ConditionalOnBean(FilterProperties.class)
public class ApiSignatureFilterConfig {
	@Autowired
	private FilterProperties filterProperties;
	@Bean
	public SignGlobalFilter signGlobalFilter(ApiSignature apiSignature) {
		SignGlobalFilter signGlobalFilter = new SignGlobalFilter();
		signGlobalFilter.setApiSignature(apiSignature);
		HashMap<String, Set<String>> excludeMap = new HashMap<String,Set<String>>();
		Map<String, Filter> filters = filterProperties.getFilters();
		Filter filter = filters.get(SignGlobalFilter.SIGN_FILTER_NAME);
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
		signGlobalFilter.setOrder(filter.getOrder());
		signGlobalFilter.setExcludeMap(excludeMap);
		return signGlobalFilter;
	}
	//@Bean
	public SignGlobalFilter signGlobalFilter1(ApiSignature apiSignature) {
		SignGlobalFilter signGlobalFilter = new SignGlobalFilter();
		signGlobalFilter.setApiSignature(apiSignature);
		Set<String> exclude = new HashSet<>();
		exclude.add("/login/login/login");
		exclude.add("/login/oauth2/authorize");
		exclude.add("/open/application/getByClientId");
		exclude.add("/login/login/callback");
		exclude.add("/login/oauth2/accessToken");
		exclude.add("/login/oauth2/getUserInfo");
		exclude.add("/login/scanQRCode");
		signGlobalFilter.setExclude(exclude);
		return signGlobalFilter;
	}
	
	@Bean
	@ConditionalOnBean({SecretService.class})
	public ApiSignature apiSignature(SecretService secretService) {
		DefaultApiSignature apiSignature = new DefaultApiSignature();
		apiSignature.setSignature(signature());
		apiSignature.setSecretService(secretService);
		return apiSignature; 
	}
	
	@Bean
	public Signature signature() {
		return new DefaultSignature();
	}
}
