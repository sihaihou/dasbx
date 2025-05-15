package com.reyco.dasbx.decrypt.endpoint;

import javax.xml.ws.Endpoint;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Endpoint.class)
public class DecryptEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(DecryptToolsIndicator.class)
	public DecryptToolsIndicator decryptToolsIndicator() {
		DecryptToolsIndicator decryptToolsIndicator = new DecryptToolsIndicator();
		return decryptToolsIndicator;
	}
}
