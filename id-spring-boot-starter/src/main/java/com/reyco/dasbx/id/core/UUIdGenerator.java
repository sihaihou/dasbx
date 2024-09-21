package com.reyco.dasbx.id.core;

import java.util.UUID;

/**
 * UUId算法Id生成器
 * @author reyco
 *
 */
public class UUIdGenerator implements IdGenerator<String> {
	
	@Override
	public String getGeneratorId() {
		return getGeneratorId("");
	}
	public static String getUUId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	public static String getGeneratorId(String prefix) {
		return prefix+getUUId();
	}
}
