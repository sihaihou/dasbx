package com.reyco.dasbx.config.filter;

import javax.servlet.DispatcherType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class TrackFilterBean {
	
	@Autowired 
	private TrackFilter trackFilter;
	@Bean
	public FilterRegistrationBean<TrackFilter> trackFilterRegistration() {
		FilterRegistrationBean<TrackFilter> registration = new FilterRegistrationBean<TrackFilter>();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(trackFilter);
		registration.addUrlPatterns("/*");
		registration.setName("trackFilter");
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return registration;
	}
}
