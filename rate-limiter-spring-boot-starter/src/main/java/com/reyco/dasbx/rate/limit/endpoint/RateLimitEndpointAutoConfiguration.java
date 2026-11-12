package com.reyco.dasbx.rate.limit.endpoint;


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
@ConditionalOnProperty(name="reyco.dasbx.rate.limit.enabled",matchIfMissing=true)
public class RateLimitEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(RateLimitToolsIndicator.class)
	public RateLimitToolsIndicator rateLimitToolsIndicator() {
		RateLimitToolsIndicator rateLimitToolsIndicator = new RateLimitToolsIndicator();
		return rateLimitToolsIndicator;
	}
}
