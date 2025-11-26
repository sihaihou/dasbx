package com.reyco.dasbx.lock.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

public class RedisDistributedLock implements DistributedLock {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public final static String SET_LUA_SCRIPT = "if redis.call('setnx', KEYS[1],ARGV[1]) == 1 then return redis.call('EXPIRE',KEYS[1],ARGV[2]) else return 0 end";// lua脚本，用来获取分布式锁

	public final static String DEL_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";// lua脚本，用来释放分布式锁

	public final static String RENEWAL_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('EXPIRE',KEYS[1],ARGV[2]) else return 0 end";// lua脚本，用来续约过期时间

	// 使用调度线程池管理所有看门狗任务
	private ScheduledExecutorService watchDogExecutor;
	/**
	 * 当前线程锁信息
	 */
	private ThreadLocal<LockInfo> lockInfoThreadLocal = new ThreadLocal<>();

	// 管理活跃的看门狗任务
	private final ConcurrentHashMap<String, ScheduledFuture<?>> watchDogTasks = new ConcurrentHashMap<>();
	/**
	 * 锁的key
	 */
	private static final String DISTRIBUTED_LOCK_KEY = "distributedLock:";
	/**
	 * key的名称
	 */
	private static final String DISTRIBUTED_LOCK_DEFAULT = "default";
	/**
	 * 锁的超时时间,默认3000
	 */
	private static final Integer DISTRIBUTED_LOCK_EXPIRE = 3000;

	private StringRedisTemplate redisTemplate;

	public RedisDistributedLock() {
		/*int corePoolSize = Math.max(4, Runtime.getRuntime().availableProcessors() * 2);
		ScheduledThreadPoolExecutor watchDogExecutor = new ScheduledThreadPoolExecutor(corePoolSize, r -> {
			Thread t = new Thread(r, "com.reyco.dasbx.lock.distributedLock-watchdog-" + r.hashCode());
			t.setDaemon(true);
			return t;
		});
		this.watchDogExecutor = watchDogExecutor;*/
	}

	public RedisDistributedLock(StringRedisTemplate redisTemplate,ScheduledExecutorService watchDogExecutor) {
		this.redisTemplate = redisTemplate;
		this.watchDogExecutor = watchDogExecutor;
	}

	public void setWatchDogExecutor(ScheduledExecutorService watchDogExecutor) {
		this.watchDogExecutor = watchDogExecutor;
	}

	public void setRedisTemplate(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void lock() {
		String lockKey = DISTRIBUTED_LOCK_KEY + DISTRIBUTED_LOCK_DEFAULT;
		String lockValue = UUID.randomUUID().toString().replace("-", "");
		LockInfo lockInfo = new LockInfo(lockKey, lockValue, DISTRIBUTED_LOCK_EXPIRE);
		lockInfoThreadLocal.set(lockInfo);
		List<String> keys = Collections.singletonList(lockKey);
		Integer expireTime = lockInfo.getExpireTime();
		String valueExpireTime = String.valueOf(expireTime / 1000);

		DefaultRedisScript<Long> script = new DefaultRedisScript<>(SET_LUA_SCRIPT, Long.class);

		// 带退避策略的获取锁
		long maxWaitTime = expireTime * 3L; // 最大等待时间
		long startTime = System.currentTimeMillis();
		long retryInterval = 10; // 初始重试间隔

		while (System.currentTimeMillis() - startTime < maxWaitTime) {
			Long result = redisTemplate.execute(script, keys, lockValue, valueExpireTime);
			if (result != null && result == 1) {
				logger.debug("加锁成功,【key: {}】,【value: {}】,【expireTime：{}】", lockKey, lockValue, expireTime);
				// 获取锁成功，启动看门狗
				startWatchDog(lockKey, lockValue, expireTime);
				return;
			}
			// 指数退避
			try {
				Thread.sleep(retryInterval);
				retryInterval = Math.min(retryInterval * 2, 100); // 最大100ms
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Interrupted while acquiring lock", e);
			}
		}

		throw new RuntimeException("Acquire lock timeout");
	}

	@Override
	public void unlock() {
		LockInfo lockInfo = lockInfoThreadLocal.get();
		// 先停止看门狗
		stopWatchDog(lockInfo.getLockKey());

		List<String> keys = new ArrayList<>();
		keys.add(lockInfo.getLockKey());

		DefaultRedisScript<Long> script = new DefaultRedisScript<>(DEL_LUA_SCRIPT, Long.class);
		try {
			Long result = redisTemplate.execute(script, keys, lockInfo.getLockValue());
			if (result != null && result == 1) {
				logger.debug("解锁成功,【key:{}】,【value:{}】", lockInfo.getLockKey(), lockInfo.getLockValue());
			}
		} catch (Exception e) {
			logger.error("解锁异常", e);
			e.printStackTrace();
		} finally {
			lockInfoThreadLocal.remove();
		}

	}

	/**
	 * 加锁
	 * 
	 * @param lockKey
	 *            锁的lockKey
	 * @param lockValue
	 *            锁的value值
	 * @param expireTime
	 *            过期时间
	 */
	@Override
	public void lock(String lockKey, String lockValue, int expireTime) {
		List<String> keys = Collections.singletonList(lockKey);
		String valueExpireTime = String.valueOf(expireTime / 1000);

		DefaultRedisScript<Long> script = new DefaultRedisScript<>(SET_LUA_SCRIPT, Long.class);

		// 带退避策略的获取锁
		long maxWaitTime = expireTime * 3L; // 最大等待时间
		long startTime = System.currentTimeMillis();
		long retryInterval = 10; // 初始重试间隔

		while (System.currentTimeMillis() - startTime < maxWaitTime) {
			Long result = redisTemplate.execute(script, keys, lockValue, valueExpireTime);
			if (result != null && result == 1) {
				logger.debug("加锁成功,【key: {}】,【value: {}】,【expireTime：{}】", lockKey, lockValue, expireTime);
				// 获取锁成功，启动看门狗
				startWatchDog(lockKey, lockValue, expireTime);
				return;
			}
			// 指数退避
			try {
				Thread.sleep(retryInterval);
				retryInterval = Math.min(retryInterval * 2, 100); // 最大100ms
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Interrupted while acquiring lock", e);
			}
		}
		throw new RuntimeException("Acquire lock timeout");
	}

	/**
	 * 解锁
	 * 
	 * @param lockKey
	 * @param lockValue
	 */
	@Override
	public void unLock(String lockKey, String lockValue) {
		// 先停止看门狗
		stopWatchDog(lockKey);
		// 然后释放锁
		List<String> keys = Collections.singletonList(lockKey);
		DefaultRedisScript<Long> script = new DefaultRedisScript<>(DEL_LUA_SCRIPT, Long.class);

		try {
			Long result = redisTemplate.execute(script, keys, lockValue);
			if (result != null && result == 1) {
				logger.debug("解锁成功, key: {}, value: {}", lockKey, lockValue);
			}
		} catch (Exception e) {
			logger.error("解锁异常", e);
		}finally {
			lockInfoThreadLocal.remove();
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
	}

	@Override
	public boolean tryLock() {
		try {
			return tryLock(0, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		String lockKey = DISTRIBUTED_LOCK_KEY + DISTRIBUTED_LOCK_DEFAULT;
		String lockValue = UUID.randomUUID().toString().replace("-", "");
		return tryLock(lockKey, lockValue, DISTRIBUTED_LOCK_EXPIRE, time, unit);
	}

	private boolean tryLock(String lockKey, String lockValue, int expireTime, long time, TimeUnit unit)
			throws InterruptedException {

		List<String> keys = Collections.singletonList(lockKey);
		String valueExpireTime = String.valueOf(expireTime / 1000);
		DefaultRedisScript<Long> script = new DefaultRedisScript<>(SET_LUA_SCRIPT, Long.class);

		long maxWaitTime = unit.toMillis(time);
		long startTime = System.currentTimeMillis();
		long retryInterval = 10;

		while (System.currentTimeMillis() - startTime < maxWaitTime) {
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException("Thread interrupted while trying to acquire lock");
			}

			Long result = redisTemplate.execute(script, keys, lockValue, valueExpireTime);
			if (result != null && result == 1) {
				logger.debug("TryLock成功, key: {}, value: {}, expireTime: {}", lockKey, lockValue, expireTime);
				startWatchDog(lockKey, lockValue, expireTime);
				
				// 保存锁信息到ThreadLocal
				lockInfoThreadLocal.set(new LockInfo(lockKey, lockValue, expireTime));
				return true;
			}

			if (time == 0) { // 立即返回
				return false;
			}

			// 指数退避
			Thread.sleep(retryInterval);
			retryInterval = Math.min(retryInterval * 2, 100);
		}

		return false;
	}

	@Override
	public Condition newCondition() {
		return null;
	}

	/**
	 * 开启看门狗
	 * 
	 * @param lockKey
	 * @param lockValue
	 * @param expireTime
	 */
	private void startWatchDog(String lockKey, String lockValue, int expireTime) {
		watchDogTasks.compute(lockKey, (key, existingFuture) -> {
			if (existingFuture != null) {
				existingFuture.cancel(false);
			}
			return watchDogExecutor.scheduleAtFixedRate(() -> {
				try {
					// 检查锁是否还是自己的
					String currentValue = redisTemplate.opsForValue().get(lockKey);
					if (!lockValue.equals(currentValue)) {
						// 锁已经被释放或过期，停止续期
						stopWatchDog(lockKey);
						return;
					}
					// 续期
					List<String> keys = Collections.singletonList(lockKey);
					String valueExpireTime = String.valueOf(expireTime / 1000);
					DefaultRedisScript<Long> script = new DefaultRedisScript<>(RENEWAL_LUA_SCRIPT, Long.class);
					Long result = redisTemplate.execute(script, keys, lockValue, valueExpireTime);
					if (result != null && result == 1) {
						logger.debug("续约成功, key: {}, value: {}, expireTime: {}", lockKey, lockValue, expireTime);
					} else {
						logger.warn("续约失败, key: {}, value: {}", lockKey, lockValue);
						stopWatchDog(lockKey);
					}
				} catch (RedisConnectionFailureException e) {
					logger.warn("Redis连接异常，跳过本次续期", e);
				} catch (Exception e) {
					logger.error("看门狗执行异常", e);
					stopWatchDog(lockKey);
				}
			}, expireTime / 3, expireTime / 3, TimeUnit.MILLISECONDS);
		});
	}

	/**
	 * 停止看门狗
	 * 
	 * @param lockKey
	 */
	private void stopWatchDog(String lockKey) {
		ScheduledFuture<?> future = watchDogTasks.remove(lockKey);
		if (future != null) {
			future.cancel(false);
		}
	}

	/**
	 * 锁信息
	 * 
	 * @author reyco
	 *
	 */
	static class LockInfo {
		private String lockKey;
		private String lockValue;
		private Integer expireTime;

		public LockInfo(String lockKey, String lockValue, Integer expireTime) {
			super();
			this.lockKey = lockKey;
			this.lockValue = lockValue;
			this.expireTime = expireTime;
		}

		public String getLockKey() {
			return lockKey;
		}

		public void setLockKey(String lockKey) {
			this.lockKey = lockKey;
		}

		public String getLockValue() {
			return lockValue;
		}

		public void setLockValue(String lockValue) {
			this.lockValue = lockValue;
		}

		public Integer getExpireTime() {
			return expireTime;
		}

		public void setExpireTime(Integer expireTime) {
			this.expireTime = expireTime;
		}
	}
}