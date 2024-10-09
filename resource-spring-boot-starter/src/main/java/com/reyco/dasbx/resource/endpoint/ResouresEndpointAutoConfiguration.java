package com.reyco.dasbx.resource.endpoint;

import javax.xml.ws.Endpoint;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Endpoint.class)
@ConditionalOnProperty(name="reyco.dasbx.resource.enabled",matchIfMissing=true)
public class ResouresEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ResourceToolsIndicator.class)
	public ResourceToolsIndicator resourceToolsIndicator() {
		ResourceToolsIndicator resourceToolsIndicator = new ResourceToolsIndicator();
		return resourceToolsIndicator;
	}
}
