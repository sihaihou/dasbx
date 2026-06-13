package com.reyco.dasbx.gateway.core.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {
	
	/**
	 *  1. 按 IP 限流（最常用）
	 * @return
	 */
	@Bean(name = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName()); // 可以根据需要修改以使用不同的key解析策略，如用户ID等。
    }
	
	/** 
	 * 2. 按用户 ID 限流
	 * @return
	 */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
            return Mono.just(userId != null ? userId : "anonymous");
        };
    }
    
    /**
     * 3. 按接口路径限流
     * @return
     */
    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> {
            String path = exchange.getRequest().getURI().getPath();
            return Mono.just(path);
        };
    }
    
    /**
     *  4. 组合限流（IP + 接口）
     * @return
     */
    @Bean
    @Primary
    public KeyResolver compositeKeyResolver() {
        return exchange -> {
            String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            String path = exchange.getRequest().getURI().getPath();
            return Mono.just(ip + ":" + path);
        };
    }
    /**
     * 邮箱
     * @return
     */
    @Bean
    public KeyResolver emailKeyResolver() {
        return exchange -> {
            String email = exchange.getRequest().getQueryParams().getFirst("email");
            return Mono.justOrEmpty(email).defaultIfEmpty("unknown");
        };
    }
}
