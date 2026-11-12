package com.reyco.dasbx.es.endpoint;


import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Endpoint.class)
@ConditionalOnProperty(name="reyco.dasbx.es.enabled",matchIfMissing=true)
public class EsEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(EsToolsIndicator.class)
	public EsToolsIndicator esToolsIndicator() {
		EsToolsIndicator esToolsIndicator = new EsToolsIndicator();
		return esToolsIndicator;
	}
}
