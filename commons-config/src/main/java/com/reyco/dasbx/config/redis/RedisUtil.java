package com.reyco.dasbx.config.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Autowired
	private RedisTemplate<String,String> redisTemplate;

	@Autowired
	private ValueOperations<String,String> valueOperations;

	@Autowired
	private HashOperations<String,String,String> hashOperations;

	@Autowired
	private ListOperations<String,String> listOperations;

	@Autowired
	private SetOperations<String,String> setOperations;

	@Autowired
	private ZSetOperations zSetOperations;
	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	/**
	 * 获取缓存对象
	 * 
	 * @param key
	 *            key
	 * @return
	 */
	public String get(String key) {
		return valueOperations.get(key);
	}
	/**
	 * 添加string类型的缓存...
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 */
	public void set(String key, String value) {
		valueOperations.set(key, value, 60 * 30L, TimeUnit.SECONDS);
	}

	/**
	 * 添加string类型的缓存...
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @param timeout
	 *            过期时间
	 * @param unit
	 *            时间单位
	 */
	public void set(String key, String value, long timeout, TimeUnit unit) {
		valueOperations.set(key, value, timeout, unit);
	}
	/**
	 * 添加bitmap下标的值
	 * @param key
	 * @param offset
	 * @param value
	 */
	public void setBit(String key, long offset, boolean value) {
		valueOperations.setBit(key, offset, value);
	}
	/**
	 * 获取bitmap下标的值
	 * @param key
	 * @param offset
	 * @return
	 */
	public Boolean getBit(String key, long offset) {
		return valueOperations.getBit(key, offset);
	}
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}
	public void expire(String key,long timeout, TimeUnit unit) {
		redisTemplate.expire(key, timeout, unit);
	}
	/**
	 * delete删除key缓存
	 * 
	 * @param key
	 *            key
	 * 
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 判断当前hash集合中是否已经存在hashKey
	 * 
	 * @param key
	 *            hash
	 * @param hashKey
	 *            hashKey
	 * @return
	 */
	public boolean hasKey(String key, String hashKey) {
		return hashOperations.hasKey(key, hashKey);
	}

	/**
	 * 获取hash集合中所有hashkey
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> keys(String key) {
		return hashOperations.keys(key);
	}

	/**
	 * 获取Hash集合中所有元素的value
	 * 
	 * @param key
	 * @return
	 */
	public List<String> values(String key) {
		return hashOperations.values(key);
	}

	/**
	 * 获取hash集合中的某个hashKey的值
	 * 
	 * @param key
	 * @param hashKey
	 * @return
	 */
	public String hget(String key, String hashKey) {
		return hashOperations.get(key, hashKey);
	}

	/**
	 * 批量获取hash集合中的hashKeys的值
	 * 
	 * @param key
	 * @param hashKeys
	 * @return
	 */
	public List<String> hgets(String key, Collection<String> hashKeys) {
		return hashOperations.multiGet(key, hashKeys);
	}

	/**
	 * 获取hash集合长度
	 * 
	 * @param key
	 * @return
	 */
	public Long sizeHash(String key) {
		return hashOperations.size(key);
	}

	/**
	 * 给指定 hash 的 hashkey 做1增减操作
	 * 
	 * @param key
	 * @param hashKey
	 * @param delta
	 * @return
	 */
	public Long increment(String key, String hashKey, long delta) {
		return hashOperations.increment(key, hashKey, delta);
	}

	/**
	 * 给指定 hash 的 hashkey 做0.1增减操作
	 * 
	 * @param key
	 * @param hashKey
	 * @param delta
	 * @return
	 */
	public Double increment(String key, String hashKey, Double delta) {
		return hashOperations.increment(key, hashKey, delta);
	}

	/**
	 * 给hash集合中存放一个hashKey元素值value
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void put(String key, String hashKey, String value) {
		hashOperations.put(key, hashKey, value);
	}

	/**
	 * 给hash集合中批量存放hashKey元素值value
	 * 
	 * @param key
	 * @param map
	 */
	public void putAll(String key, Map<String, String> map) {
		hashOperations.putAll(key, map);
	}

	/**
	 * 如果传入Hashkey对应的value已经存在，就返回存在的value，不进行替换。如果不存在，就添加Hashkey和value，返回null
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 * @return
	 */
	public Boolean putIfAbsent(String key, String hashKey, String value) {
		return hashOperations.putIfAbsent(key, hashKey, value);
	}

	/**
	 * 删除hash集合中一个或多个hashkey
	 * 
	 * @param key
	 * @param hashKeys
	 */
	public void delete(String key, String... hashKeys) {
		hashOperations.delete(key, hashKeys);
	}
	/**
	 * 获取集合中指定范围的元素
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> rangeList(String key, long start, long end){
		return listOperations.range(key, start, end);
	}
	/**
	 * 获取集合的长度
	 * @param key
	 * @return
	 */
	public Long sizeList(String key) {
		return listOperations.size(key);
	}
	/**
	 * 向集合中添加一个元素。从左到右
	 * @param key
	 * @param value
	 * @return
	 */
	public Long leftPush(String key, String value) {
		return listOperations.leftPush(key, value);
	}
	/**
	 * 向集合中添加一个或多个元素，从左到右
	 * @param key
	 * @param values
	 * @return
	 */
	public Long leftPushAll(String key, String... values) {
		return listOperations.leftPushAll(key, values);
	}
	/**
	 * 向集合中添加一个或多个元素，从左到右
	 * @param key
	 * @param values
	 * @return
	 */
	public Long leftPushAll(String key,Collection<String> values) {
		return listOperations.leftPushAll(key, values);
	}
	/**
	 * 向集合中他添加一个新元素，从右往左
	 * @param key
	 * @param value
	 * @return
	 */
	public Long rightPush(String key, String value) {
		return listOperations.rightPush(key, value);
	}
	/**
	 * 向集合中添加一个或多个新元素，从右往左
	 * @param key
	 * @param values
	 * @return
	 */
	public Long rightPushAll(String key, String... values) {
		return listOperations.rightPushAll(key, values);
	}
	/**
	 * 向集合中添加一个或多个新元素，从右往左
	 * @param key
	 * @param values
	 * @return
	 */
	public Long rightPushAll(String key, Collection<String> values) {
		return listOperations.rightPushAll(key, values);
	}
	/**
	 *  向集合中指定索引下添加一个新元素，并覆盖当前集合中指定位置
	 * @param key
	 * @param index
	 * @param value
	 */
	public void set(String key, long index, String value) {
		listOperations.set(key, index, value);
	}
	/**
	 * 根据索引获取集合中的元素
	 * @param key
	 * @param index
	 * @return
	 */
	public String index(String key, long index) {
		return listOperations.index(key, index);
	}
	/**
	 * 删除集合中所有的元素，并返回集合总第一个元素，从左往右
	 * @param key
	 * @return
	 */
	public String leftPop(String key) {
		return listOperations.leftPop(key);
	}
	/**
	 * 删除集合中所有的元素，并返回集合总第一个元素；从左往右
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public String leftPop(String key, long timeout, TimeUnit unit) {
		return listOperations.leftPop(key, timeout, unit);
	}
	/**
	 * 删除集合中所有的元素，并返回集合总最后一个元素，从右往左
	 * @param key
	 * @return
	 */
	public String rightPop(String key) {
		return listOperations.rightPop(key);
	}
	/**
	 * 删除集合中所有的元素，并返回集合总最后一个元素，从右往左
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	public String rightPop(String key, long timeout, TimeUnit unit) {
		return listOperations.rightPop(key, timeout, unit);
	}
	/**
	 * 给set集合添加多个值，集合不存在创建后再添加
	 * @param key
	 * @param value
	 * @return
	 */
	public Long add(String key, String... values) {
		return setOperations.add(key, values);
	}
	/**
	 * set中是否有这个元素
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean isMember(String key, String value) {
		return setOperations.isMember(key, value);
	}
	/**
	 * 移除set集合中多个value值
	 * @param key
	 * @param values
	 * @return
	 */
	public Long remove(String key, String... values) {
		return setOperations.remove(key, values);
	}
	/**
	 * 添加value到排序集key，或者score如果已存在则更新它。
	 * @param key
	 * @param value
	 * @param score
	 * @return
	 */
	public Boolean add(String key, String value, double score) {
		return zSetOperations.add(key, value, score);
	}
	/**
	 * 计算排序集内元素的数量，其中得分为min和max。
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Long	count(String key, double min, double max) {
		return zSetOperations.count(key, min, max);
	}
	/**
	 * 键为K的集合，索引start<=index<=end的元素子集，正序
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> rangeZSet(String key, long start, long end) {
		return zSetOperations.range(key, start, end);
	}
	/**
	 * 键为K的集合，索引start<=index<=end的元素子集，倒序
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> reverseRange(String key, long start, long end){
		return zSetOperations.reverseRange(key, start, end);
	}
	
}
