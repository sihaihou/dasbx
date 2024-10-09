package com.reyco.dasbx.oss.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class OssToolsIndicator extends AbstractToolsIndicator{
	
	public OssToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("oss").version("0.0.1-SNAPSHOT").description("Oss storage");
	}

}
