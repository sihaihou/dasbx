package com.reyco.dasbx.lua.core.exception;

public class LuaScriptException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public enum Reason {
		/**
		 * 指定名称的脚本不存在
		 */
		NOT_FOUND,

		/**
		 * 扫描或读取脚本资源失败
		 */
		LOAD_ERROR,

		/**
		 * Redis 执行脚本失败
		 */
		EXECUTION_ERROR
	}

	private final Reason reason;

	private final String scriptName;

	public LuaScriptException(Reason reason, String scriptName, String message) {
		super(message);
		this.reason = reason;
		this.scriptName = scriptName;
	}

	public LuaScriptException(Reason reason, String scriptName, String message, Throwable cause) {
		super(message, cause);
		this.reason = reason;
		this.scriptName = scriptName;
	}

	public static LuaScriptException notFound(String scriptName) {
		return new LuaScriptException(Reason.NOT_FOUND, scriptName, "Lua script not found: " + scriptName);
	}

	public static LuaScriptException loadError(String location, Throwable cause) {
		return new LuaScriptException(Reason.LOAD_ERROR, null,
				"Unable to load Lua scripts from classpath location [" + location + "]", cause);
	}

	public static LuaScriptException executionError(String scriptName, Throwable cause) {
		return new LuaScriptException(Reason.EXECUTION_ERROR, scriptName,
				"Failed to execute Lua script '" + scriptName + "'", cause);
	}

	public Reason getReason() {
		return reason;
	}

	public String getScriptName() {
		return scriptName;
	}
}
