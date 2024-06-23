package com.reyco.dasbx.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

@SuppressWarnings("all")
@Configuration
public class RedisConfig {
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Bean
	public ValueOperations getValueOperations() {
		return redisTemplate.opsForValue();
	}
	
	@Bean
	public HashOperations getHashOperations() {
		return redisTemplate.opsForHash();
	}
	@Bean
	public ListOperations getListOperations() {
		return redisTemplate.opsForList();
	}
	@Bean
	public SetOperations getSetOperations() {
		return redisTemplate.opsForSet();
	}
	
	@Bean
	public ZSetOperations getZSetOperations() {
		return redisTemplate.opsForZSet();
	}
	
}
