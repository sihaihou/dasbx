package com.reyco.dasbx.id.core;

import java.util.UUID;

/**
 * UUId算法Id生成器
 * @author reyco
 *
 */
public class UUIdGenerator implements IdGenerator {
	
	@Override
	public String nextIdStr() {
		return getUUId();
	}
	public String nextIdStr(String prefix) {
		return prefix+getUUId();
	}
	
	private static String getUUId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
