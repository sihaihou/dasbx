package com.reyco.dasbx.lock.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class LockToolsIndicator extends AbstractToolsIndicator{
	
	public LockToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("lock").version("0.0.1-SNAPSHOT").description("Distributed lock");
	}

}
