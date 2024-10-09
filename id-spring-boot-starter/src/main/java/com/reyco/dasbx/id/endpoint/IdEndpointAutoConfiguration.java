package com.reyco.dasbx.id.endpoint;

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
@ConditionalOnProperty(name="reyco.dasbx.id.enabled",matchIfMissing=true)
public class IdEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(IdToolsIndicator.class)
	public IdToolsIndicator idToolsIndicator() {
		IdToolsIndicator idToolsIndicator = new IdToolsIndicator();
		return idToolsIndicator;
	}
}
