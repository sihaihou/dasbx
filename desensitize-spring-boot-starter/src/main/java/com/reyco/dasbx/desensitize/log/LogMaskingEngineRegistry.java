package com.reyco.dasbx.desensitize.log;

/**
 * 在 Spring 初始化和 Logback 转换器之间传递日志脱敏引擎。
 */
public final class LogMaskingEngineRegistry {

	private static volatile LogMaskingEngine engine;

	private LogMaskingEngineRegistry() {
	}

	public static void registry(LogMaskingEngine maskingEngine) {
		engine = maskingEngine;
	}

	public static LogMaskingEngine get() {
		return engine;
	}

	public static void clear() {
		engine = null;
	}
}
