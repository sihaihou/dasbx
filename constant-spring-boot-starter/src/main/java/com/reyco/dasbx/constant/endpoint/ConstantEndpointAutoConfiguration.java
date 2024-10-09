package com.reyco.dasbx.constant.endpoint;

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
@ConditionalOnProperty(name="reyco.dasbx.constants.enabled",matchIfMissing=true)
public class ConstantEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ConstantToolsIndicator.class)
	public ConstantToolsIndicator constantToolsIndicator() {
		ConstantToolsIndicator constantToolsIndicator = new ConstantToolsIndicator();
		return constantToolsIndicator;
	}
}
