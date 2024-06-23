package com.reyco.dasbx.trim.autoConfiguration;

import java.util.logging.Filter;

import javax.servlet.DispatcherType;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.trim.filter.RemoveSpaceParamsFilter;
import com.reyco.dasbx.trim.properties.TrimProperties;


@Configuration
@ConditionalOnProperty(name="reyco.dasbx.trim.enabled",matchIfMissing=true)
@ConditionalOnClass(value= {Filter.class,RemoveSpaceParamsFilter.class})
@EnableConfigurationProperties(TrimProperties.class)
public class TrimFilterConfig {
	
	@Bean
	@ConditionalOnMissingBean(TrimProperties.class)
	public TrimProperties trimProperties() {
		return new TrimProperties();
	}
	/**
	 * 去除参数头尾空格过滤器
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<RemoveSpaceParamsFilter> removeSpaceParamsFilterRegistration(TrimProperties trimProperties) {
		FilterRegistrationBean<RemoveSpaceParamsFilter> registration = new FilterRegistrationBean<RemoveSpaceParamsFilter>();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(new RemoveSpaceParamsFilter());
		registration.addUrlPatterns(trimProperties.getUrlPatterns());
		registration.setName("removeSpaceFilter");
		registration.setOrder(trimProperties.getOrder());
		return registration;
	}
}
