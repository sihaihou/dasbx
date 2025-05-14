package com.reyco.dasbx.sync.redis;

/**
 * Redis同步
 * @author reyco
 *
 */
public interface RedisSync<T,R> extends RedisFullSync<R>,RedisIncrementUpdateSync<T, R>{
	
}
