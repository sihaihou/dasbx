package com.reyco.dasbx.id.core;

public interface IdGenerator<T> {
	
	String DEFAULT_IDGENERATOR = "snowflakeIdGenerator";
	
	String UUID_IDGENERATOR = "uuIdGenerator";
	
	String SNOWFLAKE_IDGENERATOR = "snowflakeIdGenerator";
	
	T getGeneratorId();
	
}
