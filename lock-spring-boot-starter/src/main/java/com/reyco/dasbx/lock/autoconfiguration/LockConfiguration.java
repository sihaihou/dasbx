package com.reyco.dasbx.lock.autoconfiguration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
@ConditionalOnProperty(name = "reyco.dasbx.lock.enabled", matchIfMissing = true)
@AutoConfigureAfter(value = { RedisAutoConfiguration.class })
public class LockConfiguration {

	@Bean
	@ConditionalOnMissingBean(ScheduledExecutorService.class)
	public ScheduledExecutorService watchDogExecutor() {
		int corePoolSize = Math.max(4, Runtime.getRuntime().availableProcessors() * 2);
		ScheduledThreadPoolExecutor watchDogExecutor = new ScheduledThreadPoolExecutor(corePoolSize, r -> {
            Thread t = new Thread(r, "com.reyco.dasbx.lock.distributedLock-watchdog-" + r.hashCode());
            t.setDaemon(true);
            return t;
        });
		return watchDogExecutor;
	}

	@Bean
	@ConditionalOnMissingBean(DistributedLock.class)
	public DistributedLock distributedLock(StringRedisTemplate redisTemplate, ScheduledExecutorService watchDogExecutor) {
		RedisDistributedLock redisDistributedLock = new RedisDistributedLock();
		redisDistributedLock.setRedisTemplate(redisTemplate);
		redisDistributedLock.setWatchDogExecutor(watchDogExecutor);
		return redisDistributedLock;
	}

	@Bean
	public DistributedLockAspect distributedLockAspect(DistributedLock distributedLock) {
		DistributedLockAspect distributedLockAspect = new DistributedLockAspect();
		distributedLockAspect.setDistributedLock(distributedLock);
		return distributedLockAspect;
	}
}
