package com.reyco.dasbx.id.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class IdToolsIndicator extends AbstractToolsIndicator{
	
	public IdToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("Id").version("0.0.1-SNAPSHOT").description("Id Generator");
	}

}
