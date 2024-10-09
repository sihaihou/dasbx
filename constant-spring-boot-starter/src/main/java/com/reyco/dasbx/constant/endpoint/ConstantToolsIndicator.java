package com.reyco.dasbx.constant.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class ConstantToolsIndicator extends AbstractToolsIndicator{
	
	public ConstantToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("constant").version("0.0.1-SNAPSHOT").description("constant");
	}

}
