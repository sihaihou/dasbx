package com.reyco.dasbx.lock.core;

import java.util.concurrent.locks.Lock;

/**
 * @author reyco
 * <pre>
 *	分布式锁
 * <pre>
 */
public interface DistributedLock extends Lock {
	/**
	 * 	加锁
	 * @param lockKey 	   锁的lockKey
	 * @param lockValue  锁的value值
	 * @param expireTime 过期时间
	 */
	void lock(String lockKey, String lockValue, int expireTime);
	/**
	 * 解锁
	 * @param lockKey	   锁的lockKey
	 * @param lockValue  锁的value值
	 */
	void unLock(String lockKey, String lockValue);
}