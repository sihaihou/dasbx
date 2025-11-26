package com.reyco.dasbx.config.lua;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
public class LuaScriptTemplate {

	private static Logger log = LoggerFactory.getLogger(LuaScriptLoader.class);

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public Long executeScript(String scriptName, List<String> keys, String... args) {
		try {
			// 强制重新加载脚本
			String luaScript = LuaScriptFactory.getLuaScript(scriptName);
			DefaultRedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
			return stringRedisTemplate.execute(script, keys, args);
		} catch (Exception e) {
			log.error("EVALSHA failed for script {}, falling back to EVAL", scriptName);
			e.printStackTrace();
			return 0L;
		}
	}
	
	public Set<String> getScripts(){
		return LuaScriptFactory.getLuaScripts();
	}
}
