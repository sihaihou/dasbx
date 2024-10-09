package com.reyco.dasbx.trim.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class TrimToolsIndicator extends AbstractToolsIndicator{
	
	public TrimToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("trim").version("0.0.1-SNAPSHOT").description("Process spaces before and after request parameters");
	}

}
