package com.reyco.dasbx.lua.autoconfiguration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.reyco.dasbx.lua.core.LuaScriptTemplate;

@Configuration
@ConditionalOnProperty(name = "reyco.dasbx.lua.enabled", matchIfMissing = true)
public class LuaConfiguration {
	
	@Bean
	public LuaScriptTemplate LuaScriptTemplate(StringRedisTemplate stringRedisTemplate) {
		LuaScriptTemplate luaScriptTemplate = new LuaScriptTemplate();
		luaScriptTemplate.setStringRedisTemplate(stringRedisTemplate);
		return luaScriptTemplate;
	}
	
}
