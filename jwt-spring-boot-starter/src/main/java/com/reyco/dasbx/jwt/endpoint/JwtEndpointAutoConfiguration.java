package com.reyco.dasbx.jwt.endpoint;

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
@ConditionalOnProperty(name="reyco.dasbx.jwt.enabled",matchIfMissing=true)
public class JwtEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(JwtToolsIndicator.class)
	public JwtToolsIndicator jwtToolsIndicator() {
		JwtToolsIndicator jwtToolsIndicator = new JwtToolsIndicator();
		return jwtToolsIndicator;
	}
}
