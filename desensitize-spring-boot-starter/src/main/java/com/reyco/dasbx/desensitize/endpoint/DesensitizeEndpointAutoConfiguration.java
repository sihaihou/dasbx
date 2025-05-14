package com.reyco.dasbx.desensitize.endpoint;

import javax.xml.ws.Endpoint;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.desensitize.autoConfiguration.DesensitizeConfiguration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Endpoint.class)
@ConditionalOnProperty(name=DesensitizeConfiguration.ENABLED,matchIfMissing=true)
public class DesensitizeEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(DesensitizeToolsIndicator.class)
	public DesensitizeToolsIndicator esToolsIndicator() {
		DesensitizeToolsIndicator esToolsIndicator = new DesensitizeToolsIndicator();
		return esToolsIndicator;
	}
}
