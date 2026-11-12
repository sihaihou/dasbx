package com.reyco.dasbx.lua.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class LuaToolsIndicator extends AbstractToolsIndicator{
	
	public LuaToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("lua").version("0.0.1-SNAPSHOT").description("Lua Script");
	}

}
