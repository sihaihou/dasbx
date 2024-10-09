package com.reyco.dasbx.actuator.tools;

import java.util.LinkedHashMap;
import java.util.Map;

public class CompositeToolsIndicator implements ToolsIndicator{
	
	private final ToolsIndicatorRegistry registry;
	
	public CompositeToolsIndicator(Map<String, ToolsIndicator> indicators) {
		this(new DefaultToolsIndicatorRegistry(indicators));
	}
	
	public CompositeToolsIndicator(ToolsIndicatorRegistry registry) {
		this.registry = registry;
	}

	public ToolsIndicatorRegistry getRegistry() {
		return this.registry;
	}
	@Override
	public Tools tools() {
		Map<String, Tools> healths = new LinkedHashMap<>();
		for (Map.Entry<String, ToolsIndicator> entry : this.registry.getAll().entrySet()) {
			healths.put(entry.getKey(), entry.getValue().tools());
		}
		return new Tools.Builder("root", "v.1.0.0",healths).build();
	}

}
