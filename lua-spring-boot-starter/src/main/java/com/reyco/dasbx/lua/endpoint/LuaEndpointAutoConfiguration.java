package com.reyco.dasbx.lua.endpoint;


import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Endpoint.class)
@ConditionalOnProperty(name="reyco.dasbx.lua.enabled",matchIfMissing=true)
public class LuaEndpointAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(LuaToolsIndicator.class)
	public LuaToolsIndicator luaToolsIndicator() {
		LuaToolsIndicator luaToolsIndicator = new LuaToolsIndicator();
		return luaToolsIndicator;
	}
}
