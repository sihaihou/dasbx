package com.reyco.dasbx.decrypt.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class DecryptToolsIndicator extends AbstractToolsIndicator{
	
	public DecryptToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("decrypt").version("0.0.1-SNAPSHOT").description("Decrypt Tools");
	}

}
