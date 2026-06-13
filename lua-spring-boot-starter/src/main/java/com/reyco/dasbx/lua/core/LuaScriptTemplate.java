package com.reyco.dasbx.lua.core;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

public class LuaScriptTemplate {

	private static Logger log = LoggerFactory.getLogger(LuaScriptLoader.class);

	private StringRedisTemplate stringRedisTemplate;
	
	// 缓存已经构建好的 RedisScript 对象
    private final Map<String, DefaultRedisScript<Long>> scriptCache = new ConcurrentHashMap<>();
	
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
		try {
			// 从缓存中获取，确保同一个脚本只初始化一次
            DefaultRedisScript<Long> redisScript = scriptCache.computeIfAbsent(scriptName, name -> {
                String luaScript = LuaScriptFactory.getLuaScript(name);
                DefaultRedisScript<Long> script = new DefaultRedisScript<>();
                script.setScriptText(luaScript); // 设置脚本内容
                script.setResultType(Long.class);
                return script;
            });
            // 第一次执行时，Spring 会自动触发 SCRIPT LOAD 并缓存 SHA1
            // 之后的请求都会直接走高性能的 EVALSHA，且不再打印 ERROR 日志
			return stringRedisTemplate.execute(redisScript, keys, args);
		} catch (Exception e) {
			log.error("Execute lua script {} failed", scriptName, e);
			e.printStackTrace();
			return 0L;
		}
	}
	
	public Set<String> getScripts(){
		return LuaScriptFactory.getLuaScripts();
	}
}
