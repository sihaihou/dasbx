package com.reyco.dasbx.trim.endpoint;

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
@ConditionalOnProperty(name="reyco.dasbx.trim.enabled",matchIfMissing=true)
public class TrimEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(TrimToolsIndicator.class)
	public TrimToolsIndicator trimToolsIndicator() {
		TrimToolsIndicator trimToolsIndicator = new TrimToolsIndicator();
		return trimToolsIndicator;
	}
}
