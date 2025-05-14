package com.reyco.dasbx.rabbitmq.configuration.endpoint;

import com.reyco.dasbx.actuator.tools.AbstractToolsIndicator;
import com.reyco.dasbx.actuator.tools.Tools.Builder;

public class RabbitmqToolsIndicator extends AbstractToolsIndicator{
	
	public RabbitmqToolsIndicator() {
		super();
	}

	@Override
	protected void doToolsCheck(Builder builder) throws Exception {
		builder.name("rabbitmq").version("0.0.1-SNAPSHOT").description("Rabbitmq Tools");
	}

}
