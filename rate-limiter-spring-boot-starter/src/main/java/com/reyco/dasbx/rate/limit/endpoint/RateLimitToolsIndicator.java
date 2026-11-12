package com.reyco.dasbx.rate.limit.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class RateLimitToolsIndicator extends AbstractToolsIndicator{
	
	public RateLimitToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("Rate-limit").version("0.0.1-SNAPSHOT").description("Rate-limit Redis限流");
	}

}
