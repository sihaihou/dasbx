package com.reyco.dasbx.actuator.tools;

import java.util.Map;
import java.util.function.Function;

import org.springframework.util.Assert;

public class ToolsIndicatorRegistryFactory {
	
	private final Function<String, String> toolsIndicatorNameFactory;
	
	public ToolsIndicatorRegistryFactory() {
		this(new ToolsIndicatorNameFactory());
	}
	
	public ToolsIndicatorRegistryFactory(
			Function<String, String> toolsIndicatorNameFactory) {
		this.toolsIndicatorNameFactory = toolsIndicatorNameFactory;
	}
	
	public ToolsIndicatorRegistry createToolsIndicatorRegistry(
			Map<String, ToolsIndicator> toolsIndicators) {
		Assert.notNull(toolsIndicators, "toolsIndicators must not be null");
		return initialize(new DefaultToolsIndicatorRegistry(), toolsIndicators);
	}
	
	protected <T extends ToolsIndicatorRegistry> T initialize(T registry,
			Map<String, ToolsIndicator> healthIndicators) {
		for (Map.Entry<String, ToolsIndicator> entry : healthIndicators.entrySet()) {
			String name = this.toolsIndicatorNameFactory.apply(entry.getKey());
			registry.register(name, entry.getValue());
		}
		return registry;
	}
	
}
