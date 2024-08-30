package com.reyco.dasbx.lock.autoconfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.reyco.dasbx.lock.core.DistributedLock;
import com.reyco.dasbx.lock.core.DistributedLockAspect;
import com.reyco.dasbx.lock.core.RedisDistributedLock;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.lock.enabled",matchIfMissing=true)
@AutoConfigureAfter(value= {RedisAutoConfiguration.class})
public class LockConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ExecutorService.class)
	public ExecutorService executorService() {
		ExecutorService executorService = new ThreadPoolExecutor(8, 16, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),new ThreadFactory() {
			LongAdder count = new LongAdder();
			@Override
			public Thread newThread(Runnable r) {
				count.increment();
				Thread thread = new Thread(r);
				thread.setName("com.reyco.dasbx.distributedLock-"+count.intValue());
				thread.setDaemon(false);
				return thread;
			}
		},new ThreadPoolExecutor.CallerRunsPolicy());
		return executorService;
	}
	
	@Bean
	@ConditionalOnMissingBean(DistributedLock.class)
	public DistributedLock distributedLock(StringRedisTemplate redisTemplate,ExecutorService executorService) {
		RedisDistributedLock redisDistributedLock = new RedisDistributedLock();
		redisDistributedLock.setExecutorService(executorService);
		redisDistributedLock.setRedisTemplate(redisTemplate);
		return redisDistributedLock;
	}
	
	@Bean
	public DistributedLockAspect distributedLockAspect(DistributedLock distributedLock) {
		DistributedLockAspect distributedLockAspect = new DistributedLockAspect();
		distributedLockAspect.setDistributedLock(distributedLock);
		return distributedLockAspect;
	}
}
