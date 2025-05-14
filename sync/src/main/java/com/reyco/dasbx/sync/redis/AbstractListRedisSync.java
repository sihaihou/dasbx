package com.reyco.dasbx.sync.redis;

import java.util.function.Supplier;

import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;
import com.reyco.dasbx.sync.exception.SyncException;

/**
 * Redis同步接口
 * @author reyco
 *
 */
public abstract class AbstractListRedisSync<T,E extends RedisDocument> extends AbstractRedisFullSync implements RedisIncrementUpdateSync<T, Integer> {
	
	private static final int DEFAULT_RETRIES = 3;
	private static final String DEFAULT_PREFIX = "default:";
	
	private RedisUtil redisUtil;
	
	public AbstractListRedisSync(RedisUtil redisUtil) {
		super();
		this.redisUtil = redisUtil;
	}

	@Override
	public Integer incrementUpdateSync(Supplier<T> supplier) throws SyncException {
		int retries = getRetries();
		retries += 1;
		boolean flag = true;
		while(flag && retries>0) {
			try {
				T t = supplier.get();
				E e = getRedisDocumentByPrimaryKeyId(t);
				String key = getKey(e);
				String value = JsonUtils.objToJson(e);
				redisUtil.set(key, value);
				flag = false;
			} catch (Exception e2) {
				retries--;
			}
		}
		return flag ? 0 : 1;
	}
	
	@Override
	public Integer incrementDeleteSync(Supplier<T> supplier) throws SyncException {
		int retries = getRetries();
		retries += 1;
		boolean flag = true;
		while(flag && retries>0) {
			try {
				T t = supplier.get();
				E e = getRedisDocumentByPrimaryKeyId(t);
				String key = getKey(e);
				redisUtil.delete(key);
				flag = false;
			} catch (Exception e2) {
				retries--;
			}
		}
		return flag ? 0 : 1;
	}
	/**
	 * 获取Key
	 * @return
	 */
	protected String getKey(E e) {
		String primaryKeyId = e.getPrimaryKeyId();
		String keyPrefix = getKeyPrefix();
		String key = (keyPrefix+":"+primaryKeyId).replace("::", ":");
		return key;
	}
	/**
	 * 默认重试次数
	 * @return
	 */
	protected int getRetries() {
		return DEFAULT_RETRIES;
	}
	/**
	 * 获取Key前缀
	 * @return
	 */
	protected String getKeyPrefix() {
		return DEFAULT_PREFIX;
	}
	/**
	 * 获取元素对象
	 * @param t
	 * @return
	 */
	protected abstract E getRedisDocumentByPrimaryKeyId(T t);
}
