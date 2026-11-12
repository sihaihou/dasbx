package com.reyco.dasbx.lua.core;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.Assert;

import com.reyco.dasbx.lua.core.exception.LuaScriptException;

public class LuaScriptTemplate {

	private StringRedisTemplate stringRedisTemplate;

	// 缓存已经构建好的 RedisScript 对象
	private final Map<ScriptCacheKey, DefaultRedisScript<?>> scriptCache = new ConcurrentHashMap<>();

	public LuaScriptTemplate() {
		// TODO Auto-generated constructor stub
	}

	public LuaScriptTemplate(StringRedisTemplate stringRedisTemplate) {
		super();
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public Long executeScript(String scriptName, List<String> keys, String... args) {
		return executeScript(scriptName, Long.class, keys,  args);
	}
	/**
	 * 执行脚本
	 * @param scriptName	脚本名称
	 * @param keys			keys
	 * @param resultType	返回值类型：Long、Boolean、String、List
	 * @param args
	 * @return
	 */
	public <T> T executeScript(String scriptName,Class<T> resultType, List<String> keys, String... args) {
		Assert.hasText(scriptName, "'scriptName' must not be null or blank");
		Assert.notNull(resultType, "'resultType' must not be null");
		validateResultType(resultType);

		if (stringRedisTemplate == null) {
			throw new IllegalStateException("StringRedisTemplate must be configured before executing Lua scripts");
		}

		// 脚本不存在、资源读取失败时，LuaScriptException 从这里直接抛出，不会被下面的 Redis 执行异常处理覆盖。
        DefaultRedisScript<T> redisScript = getRedisScript(scriptName, resultType);
		try {
			// 第一次执行时，Spring 会自动触发 SCRIPT LOAD 并缓存 SHA1
			// 之后的请求都会直接走高性能的 EVALSHA，且不再打印 ERROR 日志
			return stringRedisTemplate.execute(redisScript, keys, (Object[]) args);
		} catch (RuntimeException ex) {
			throw LuaScriptException.executionError(scriptName, ex);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> DefaultRedisScript<T> getRedisScript(String scriptName, Class<T> resultType) {
		ScriptCacheKey cacheKey = new ScriptCacheKey(scriptName, resultType);

		DefaultRedisScript<?> redisScript = scriptCache.computeIfAbsent(cacheKey, key -> {
			String luaScript = LuaScriptFactory.getLuaScript(key.getScriptName());
			DefaultRedisScript<T> script = new DefaultRedisScript<>();
			script.setScriptText(luaScript);
			script.setResultType(resultType);
			return script;
		});

		return (DefaultRedisScript<T>) redisScript;
	}

	public Set<String> getScripts() {
		return LuaScriptFactory.getLuaScripts();
	}

	private void validateResultType(Class<?> resultType) {
		boolean supported = resultType == Long.class || resultType == Boolean.class || resultType == String.class
				|| resultType == List.class;

		if (!supported) {
			throw new IllegalArgumentException("Unsupported Lua result type: " + resultType.getName()
					+ ". Supported types are Long, Boolean, String and List");
		}
	}

	private static final class ScriptCacheKey {

		private final String scriptName;

		private final Class<?> resultType;

		private ScriptCacheKey(String scriptName, Class<?> resultType) {
			this.scriptName = scriptName;
			this.resultType = resultType;
		}

		public String getScriptName() {
			return scriptName;
		}

		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (!(object instanceof ScriptCacheKey)) {
				return false;
			}
			ScriptCacheKey that = (ScriptCacheKey) object;
			return scriptName.equals(that.scriptName) && resultType.equals(that.resultType);
		}

		@Override
		public int hashCode() {
			return Objects.hash(scriptName, resultType);
		}
	}
}
