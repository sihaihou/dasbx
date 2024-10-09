package com.reyco.dasbx.resource.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class ResourceToolsIndicator extends AbstractToolsIndicator{
	
	public ResourceToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("resource").version("0.0.1-SNAPSHOT").description("System log collector");
	}

}
