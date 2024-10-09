package com.reyco.dasbx.lock.endpoint;

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
@ConditionalOnProperty(name="reyco.dasbx.lock.enabled",matchIfMissing=true)
public class LockEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(LockToolsIndicator.class)
	public LockToolsIndicator lockToolsIndicator() {
		LockToolsIndicator lockToolsIndicator = new LockToolsIndicator();
		return lockToolsIndicator;
	}
}
