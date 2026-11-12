package com.reyco.dasbx.lua.autoconfiguration;


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.reyco.dasbx.lua.core.LuaScriptTemplate;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(StringRedisTemplate.class)
@ConditionalOnProperty(name = "reyco.dasbx.lua.enabled", matchIfMissing = true)
public class LuaConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(LuaScriptTemplate.class)
	public LuaScriptTemplate LuaScriptTemplate(StringRedisTemplate stringRedisTemplate) {
		LuaScriptTemplate luaScriptTemplate = new LuaScriptTemplate();
		luaScriptTemplate.setStringRedisTemplate(stringRedisTemplate);
		return luaScriptTemplate;
	}
	
}
