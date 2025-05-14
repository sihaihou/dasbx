package com.reyco.dasbx.sync.redis;

import com.reyco.dasbx.sync.exception.SyncException;

/**
 * 抽象Redis实现类
 * @author reyco
 *
 */
public abstract class AbstractRedisFullSync implements RedisFullSync<Integer>{
	
	@Override
	public Integer fullSync() throws SyncException {
		throw new RuntimeException("Redis not support full sync.");
	}
	
}
