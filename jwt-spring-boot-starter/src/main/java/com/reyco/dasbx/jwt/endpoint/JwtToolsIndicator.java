package com.reyco.dasbx.jwt.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class JwtToolsIndicator extends AbstractToolsIndicator{
	
	public JwtToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("jwt").version("0.0.1-SNAPSHOT").description("Jwt token");
	}

}
