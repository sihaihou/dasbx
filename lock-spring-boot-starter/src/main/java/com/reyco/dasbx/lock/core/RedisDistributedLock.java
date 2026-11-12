package com.reyco.dasbx.lock.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

	public final static String SET_LUA_SCRIPT = "if redis.call('setnx', KEYS[1],ARGV[1]) == 1 then return redis.call('PEXPIRE',KEYS[1],ARGV[2]) else return 0 end";// lua脚本，用来获取分布式锁

	public final static String DEL_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";// lua脚本，用来释放分布式锁

	public final static String RENEWAL_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('PEXPIRE',KEYS[1],ARGV[2]) else return 0 end";// lua脚本，用来续约过期时间

	// 使用调度线程池管理所有看门狗任务
	private ScheduledExecutorService watchDogExecutor;
	/**
	 * 当前线程锁信息
	 */
	private ThreadLocal<LockInfo> lockInfoThreadLocal = new ThreadLocal<>();

	// 管理活跃的看门狗任务
	private final ConcurrentHashMap<LockOwner, ScheduledFuture<?>> watchDogTasks = new ConcurrentHashMap<>();
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
	/**
	 * 锁的最小时间,默认100
	 */
	private static final int MIN_EXPIRE_TIME = 100;

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
		List<String> keys = Collections.singletonList(lockKey);
		Integer expireTime = lockInfo.getExpireTime();

		DefaultRedisScript<Long> script = new DefaultRedisScript<>(SET_LUA_SCRIPT, Long.class);

		// 带退避策略的获取锁
		long maxWaitTime = expireTime * 3L; // 最大等待时间
		long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(maxWaitTime);
		long retryNanos = TimeUnit.MILLISECONDS.toNanos(10);
		long maxRetryNanos = TimeUnit.MILLISECONDS.toNanos(100);
		
		while (deadline - System.nanoTime() > 0) {
			Long result = redisTemplate.execute(script, keys, lockValue, String.valueOf(expireTime));
			if (result != null && result == 1) {
				logger.debug("加锁成功,【key: {}】,【value: {}】,【expireTime：{}】", lockKey, lockValue, expireTime);
				// 获取锁成功，启动看门狗
				startWatchDog(lockKey, lockValue, expireTime);
				lockInfoThreadLocal.set(lockInfo);
				return;
			}
			// 指数退避
			try {
				long remaining =  deadline - System.nanoTime();
				if(remaining<=0) {
					break;
				}
				TimeUnit.NANOSECONDS.sleep(Math.min(retryNanos, remaining));
				retryNanos = Math.min(retryNanos * 2, maxRetryNanos);
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
		if(lockInfo==null) {
			return;
		}
		LockOwner lockOwner= new LockOwner(lockInfo.getLockKey(), lockInfo.getLockValue());
		// 先停止看门狗
		stopWatchDog(lockOwner);

		List<String> keys = new ArrayList<>();
		keys.add(lockInfo.getLockKey());

		DefaultRedisScript<Long> script = new DefaultRedisScript<>(DEL_LUA_SCRIPT, Long.class);
		try {
			Long result = redisTemplate.execute(script, keys, String.valueOf(lockInfo.getLockValue()));
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
		if (expireTime < MIN_EXPIRE_TIME) {
	        throw new IllegalArgumentException("expireTime must be at least " + MIN_EXPIRE_TIME + "ms, but was " + expireTime + "ms");
	    }
		List<String> keys = Collections.singletonList(lockKey);

		DefaultRedisScript<Long> script = new DefaultRedisScript<>(SET_LUA_SCRIPT, Long.class);

		// 带退避策略的获取锁
		long maxWaitTime = expireTime * 3L; // 最大等待时间
		long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(maxWaitTime);
		long retryNanos = TimeUnit.MILLISECONDS.toNanos(10);
		long maxRetryNanos = TimeUnit.MILLISECONDS.toNanos(100);
		
		while (deadline - System.nanoTime() > 0) {
			Long result = redisTemplate.execute(script, keys, lockValue, String.valueOf(expireTime));
			if (result != null && result == 1) {
				logger.debug("加锁成功,【key: {}】,【value: {}】,【expireTime：{}】", lockKey, lockValue, expireTime);
				// 获取锁成功，启动看门狗
				startWatchDog(lockKey, lockValue, expireTime);
				return;
			}
			// 指数退避
			try {
				long remaining =  deadline - System.nanoTime();
				if(remaining<=0) {
					break;
				}
				TimeUnit.NANOSECONDS.sleep(Math.min(retryNanos, remaining));
				retryNanos = Math.min(retryNanos * 2, maxRetryNanos);
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
		LockOwner lockOwner= new LockOwner(lockKey, lockValue);
		// 先停止看门狗
		stopWatchDog(lockOwner);
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

	private boolean tryLock(String lockKey, String lockValue, int expireTime, long time, TimeUnit unit) throws InterruptedException {
		if (expireTime < MIN_EXPIRE_TIME) {
	        throw new IllegalArgumentException("expireTime must be at least " + MIN_EXPIRE_TIME + "ms, but was " + expireTime + "ms");
	    }
		List<String> keys = Collections.singletonList(lockKey);
		DefaultRedisScript<Long> script = new DefaultRedisScript<>(SET_LUA_SCRIPT, Long.class);

		long waitNanos = unit.toNanos(Math.max(0L, time));
		long deadline = System.nanoTime() + waitNanos;
		long retryNanos = TimeUnit.MILLISECONDS.toNanos(10);
		long maxRetryNanos = TimeUnit.MILLISECONDS.toNanos(100);
		boolean firstAttempt = true;
		
		while(true) {
			if (Thread.currentThread().isInterrupted()) {
				throw new InterruptedException("Thread interrupted while trying to acquire lock");
			}
			// 第一次必须立即尝试；后续重试前先检查是否超时
			if (!firstAttempt) {
		        if (deadline - System.nanoTime() <= 0) {
		            return false;
		        }
		    }
		    firstAttempt = false;
		    
		    // 尝试加锁
			Long result = redisTemplate.execute(script, keys, lockValue, String.valueOf(expireTime));
			if (result != null && result == 1) {
				logger.debug("TryLock成功, key: {}, value: {}, expireTime: {}", lockKey, lockValue, expireTime);
				startWatchDog(lockKey, lockValue, expireTime);
				
				// 保存锁信息到ThreadLocal
				lockInfoThreadLocal.set(new LockInfo(lockKey, lockValue, expireTime));
				return true;
			}
			
			// 是否超时
			long remaining = deadline - System.nanoTime();
			if (remaining <= 0) {
			    return false;
			}

			// 指数退避
			TimeUnit.NANOSECONDS.sleep(Math.min(retryNanos, remaining));
			retryNanos = Math.min(retryNanos * 2, maxRetryNanos);
		}
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
		LockOwner lockOwner = new LockOwner(lockKey, lockValue);
		watchDogTasks.compute(lockOwner, (key, existingFuture) -> {
			if (existingFuture != null) {
				existingFuture.cancel(false);
			}
			return watchDogExecutor.scheduleWithFixedDelay(() -> {
				try {
					// 检查锁是否还是自己的
					String currentValue = redisTemplate.opsForValue().get(lockKey);
					if (!lockValue.equals(currentValue)) {
						// 锁已经被释放或过期，停止续期
						stopWatchDog(lockOwner);
						return;
					}
					// 续期
					List<String> keys = Collections.singletonList(lockKey);
					DefaultRedisScript<Long> script = new DefaultRedisScript<>(RENEWAL_LUA_SCRIPT, Long.class);
					Long result = redisTemplate.execute(script, keys, lockValue, String.valueOf(expireTime));
					if (result != null && result == 1) {
						logger.debug("续约成功, key: {}, value: {}, expireTime: {}", lockKey, lockValue, expireTime);
					} else {
						logger.warn("续约失败, key: {}, value: {}", lockKey, lockValue);
						stopWatchDog(lockOwner);
					}
				} catch (RedisConnectionFailureException e) {
					logger.warn("Redis连接异常，跳过本次续期", e);
				} catch (Exception e) {
					logger.error("看门狗执行异常", e);
					stopWatchDog(lockOwner);
				}
			}, expireTime / 3, expireTime / 3, TimeUnit.MILLISECONDS);
		});
	}

	/**
	 * 停止看门狗
	 * 
	 * @param LockOwner
	 */
	private void stopWatchDog(LockOwner lockOwner) {
		ScheduledFuture<?> future = watchDogTasks.remove(lockOwner);
		if (future != null) {
			future.cancel(false);
		}
	}
	private static final class LockOwner {
	    private final String lockKey;
	    private final String lockValue;

	    private LockOwner(String lockKey, String lockValue) {
	        this.lockKey = lockKey;
	        this.lockValue = lockValue;
	    }
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) {
	            return true;
	        }
	        if (!(obj instanceof LockOwner)) {
	            return false;
	        }
	        LockOwner other = (LockOwner) obj;
	        return Objects.equals(lockKey, other.lockKey)
	                && Objects.equals(lockValue, other.lockValue);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(lockKey, lockValue);
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