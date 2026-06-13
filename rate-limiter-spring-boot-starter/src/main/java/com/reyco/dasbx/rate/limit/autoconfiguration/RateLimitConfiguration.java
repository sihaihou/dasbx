package com.reyco.dasbx.rate.limit.autoconfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.lua.autoconfiguration.LuaConfiguration;
import com.reyco.dasbx.lua.core.LuaScriptTemplate;
import com.reyco.dasbx.rate.limit.core.DynamicRateLimitAspect;
import com.reyco.dasbx.rate.limit.core.DynamicRateLimitService;
import com.reyco.dasbx.rate.limit.core.SlidingWindowRateLimitAspect;
import com.reyco.dasbx.rate.limit.core.TokenBucketRateLimitAspect;
import com.reyco.dasbx.rate.limit.exception.RateLimitExceptionStrategy;
import com.reyco.dasbx.rate.limit.properties.DynamicRateLimitProperties;

@Configuration
@ConditionalOnProperty(name = "reyco.dasbx.rate.limit.enabled", matchIfMissing = true)
@EnableConfigurationProperties(DynamicRateLimitProperties.class)
@AutoConfigureAfter(value = { LuaConfiguration.class })
public class RateLimitConfiguration {
	
	@Bean
	public TokenBucketRateLimitAspect tokenBucketRateLimitAspect(LuaScriptTemplate luaScriptTemplate) {
		TokenBucketRateLimitAspect tokenBucketRateLimitAspect = new TokenBucketRateLimitAspect();
		tokenBucketRateLimitAspect.setLuaScriptTemplate(luaScriptTemplate);
		return tokenBucketRateLimitAspect;
	}
	
	@Bean
	public SlidingWindowRateLimitAspect slidingWindowRateLimitAspect(LuaScriptTemplate luaScriptTemplate) {
		SlidingWindowRateLimitAspect slidingWindowRateLimitAspect = new SlidingWindowRateLimitAspect();
		slidingWindowRateLimitAspect.setLuaScriptTemplate(luaScriptTemplate);
		return slidingWindowRateLimitAspect;
	}
	
	@Bean
	public RateLimitExceptionStrategy rateLimitExceptionStrategy() {
		RateLimitExceptionStrategy rateLimitExceptionStrategy = new RateLimitExceptionStrategy();
		return rateLimitExceptionStrategy;
	}
	
	@Bean
	public DynamicRateLimitService dynamicRateLimitService(DynamicRateLimitProperties dynamicRateLimitProperties) {
		DynamicRateLimitService dynamicRateLimitService = new DynamicRateLimitService();
		dynamicRateLimitService.setDynamicRateLimitProperties(dynamicRateLimitProperties);
		return dynamicRateLimitService;
	}
	@Bean
	public DynamicRateLimitAspect dynamicRateLimitAspect(LuaScriptTemplate luaScriptTemplate,DynamicRateLimitService dynamicRateLimitService) {
		DynamicRateLimitAspect dynamicRateLimitAspect = new DynamicRateLimitAspect();
		dynamicRateLimitAspect.setLuaScriptTemplate(luaScriptTemplate);
		dynamicRateLimitAspect.setDynamicRateLimitService(dynamicRateLimitService);
		return dynamicRateLimitAspect;
	}
}
