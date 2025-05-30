package com.reyco.dasbx.redis.endpoint;

import javax.xml.ws.Endpoint;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Endpoint.class)
public class RedisEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(RedisToolsIndicator.class)
	public RedisToolsIndicator redisToolsIndicator() {
		RedisToolsIndicator redisToolsIndicator = new RedisToolsIndicator();
		return redisToolsIndicator;
	}
}
