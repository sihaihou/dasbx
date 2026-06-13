package com.reyco.dasbx.gateway.core.redis;

import javax.annotation.PreDestroy;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {
	
	private volatile LettuceConnectionFactory currentConnectionFactory;
	
	@Bean
	@ConditionalOnMissingBean
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
	public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
		 // 关闭旧的连接工厂
        if (currentConnectionFactory != null) {
            currentConnectionFactory.destroy();
        }
        
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
		config.setDatabase(redisProperties.getDatabase());
		config.setPassword(redisProperties.getPassword());
		
		GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<Object>();
		Pool pool = redisProperties.getLettuce().getPool();
		
		poolConfig.setMaxTotal(pool.getMaxActive());
		poolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
		poolConfig.setMaxIdle(pool.getMaxIdle());
		poolConfig.setMinIdle(pool.getMinIdle());
		
		LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration
				.builder()
				.commandTimeout(redisProperties.getTimeout())
				.poolConfig(poolConfig)
				.build();
		
		LettuceConnectionFactory  factory = new LettuceConnectionFactory(config,clientConfig);
		factory.afterPropertiesSet();
		currentConnectionFactory = factory;
		return factory;
	}
	
	@Configuration
	public class RedisUtilAutoConfiguration {
		
		@Bean
		@ConditionalOnMissingBean
		public RedisUtil redisUtil(RedisTemplate<String,String> redisTemplate) {
			RedisUtil redisUtil = new RedisUtil(redisTemplate);
			return redisUtil;
		}
	}
	
	@PreDestroy
    public void destroy() {
        if (currentConnectionFactory != null) {
            currentConnectionFactory.destroy();
        }
    }
}
