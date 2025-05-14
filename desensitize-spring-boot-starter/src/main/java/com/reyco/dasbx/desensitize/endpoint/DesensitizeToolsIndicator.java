package com.reyco.dasbx.desensitize.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class DesensitizeToolsIndicator extends AbstractToolsIndicator{
	
	public DesensitizeToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("desensitize").version("0.0.1-SNAPSHOT").description("Desensitize Tools");
	}

}
