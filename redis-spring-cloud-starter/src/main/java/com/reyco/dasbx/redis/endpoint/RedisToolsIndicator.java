package com.reyco.dasbx.redis.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class RedisToolsIndicator extends AbstractToolsIndicator{
	
	public RedisToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("redis").version("0.0.1-SNAPSHOT").description("Redis Tools");
	}

}
