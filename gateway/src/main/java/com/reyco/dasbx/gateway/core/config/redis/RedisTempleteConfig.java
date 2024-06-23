package com.reyco.dasbx.gateway.core.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTempleteConfig {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		if (factory instanceof LettuceConnectionFactory) {
			((LettuceConnectionFactory) factory).setValidateConnection(true);
		}
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(genericJackson2JsonRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	@RefreshScope
	public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
		redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
		redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
		
		GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<Object>();
		Pool pool = redisProperties.getJedis().getPool();
		genericObjectPoolConfig.setMaxTotal(pool.getMaxActive());
		genericObjectPoolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
		genericObjectPoolConfig.setMaxIdle(pool.getMaxIdle());
		genericObjectPoolConfig.setMinIdle(pool.getMinIdle());
		LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration
				.builder()
				.commandTimeout(redisProperties.getTimeout())
				.poolConfig(genericObjectPoolConfig)
				.build();
		RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration,lettucePoolingClientConfiguration);
		return redisConnectionFactory;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
