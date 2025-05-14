package com.reyco.dasbx.lock.autoconfiguration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.reyco.dasbx.lock.core.DistributedLock;
import com.reyco.dasbx.lock.core.DistributedLockAspect;
import com.reyco.dasbx.lock.core.RedisDistributedLock;
import com.reyco.dasbx.redis.auto.configuration.RedisAutoConfiguration;

@Configuration
@ConditionalOnProperty(name="reyco.dasbx.lock.enabled",matchIfMissing=true)
@AutoConfigureAfter(value= {RedisAutoConfiguration.class})
public class LockConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ExecutorService.class)
	public ExecutorService executorService() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		return new ThreadPoolExecutor(availableProcessors, availableProcessors*2+1, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(),new ThreadFactory() {
			LongAdder count = new LongAdder();
			@Override
			public Thread newThread(Runnable r) {
				count.increment();
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				thread.setName("com.reyco.dasbx.distributedLock.thread-"+count.intValue());
				return thread;
			}
		},new ThreadPoolExecutor.AbortPolicy());
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
