package com.reyco.dasbx.id.core;

public interface IdGenerator {

	String DEFAULT_IDGENERATOR = "snowflakeIdGenerator";

	String UUID_IDGENERATOR = "uuIdGenerator";

	String SNOWFLAKE_IDGENERATOR = "snowflakeIdGenerator";

	/**
	 * 获取字符串格式 ID（通用）
	 */
	String nextIdStr();

	/**
	 * 统一默认获取方法
	 */
	default String getGeneratorId() {
		return nextIdStr();
	}

	/**
	 * 新增：获取长整型数字 ID 默认抛出不支持异常，由具体支持数字 ID 的实现类去覆写
	 */
	default Long nextIdLong() {
		throw new UnsupportedOperationException("当前 ID 生成策略不支持返回 Long 类型数字 ID！");
	}
}
