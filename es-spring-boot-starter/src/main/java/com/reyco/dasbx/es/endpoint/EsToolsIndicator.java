package com.reyco.dasbx.es.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class EsToolsIndicator extends AbstractToolsIndicator{
	
	public EsToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("es").version("0.0.1-SNAPSHOT").description("Es Tools");
	}

}
