package com.reyco.dasbx.sync.redis;

public interface RedisDocument {
	/**
	 * es文档的id
	 * @return
	 */
	String getPrimaryKeyId();
	
	KeyType getKeyType();
	
	String getkey();
	
	public enum KeyType{
		STRING,
		HASH,
		LIST,
		SET,
		ZSET;
	}
}
