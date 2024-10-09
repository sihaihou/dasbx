package com.reyco.dasbx.actuator.tools;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class DefaultToolsIndicatorRegistry implements ToolsIndicatorRegistry{
	
	private final Object monitor = new Object();
	
	private Map<String, ToolsIndicator> toolsIndicators;
	
	public DefaultToolsIndicatorRegistry() {
		this(new LinkedHashMap<>());
	}
	
	public DefaultToolsIndicatorRegistry(Map<String, ToolsIndicator> toolsIndicators) {
		Assert.notNull(toolsIndicators, "toolsIndicators must not be null");
		this.toolsIndicators = toolsIndicators;
	}

	@Override
	public void register(String name, ToolsIndicator toolsIndicator) {
		Assert.notNull(toolsIndicator, "toolsIndicator must not be null");
		Assert.notNull(name, "name must not be null");
		synchronized (this.monitor) {
			ToolsIndicator existing = this.toolsIndicators.putIfAbsent(name,toolsIndicator);
			if (existing != null) {
				throw new IllegalStateException("ToolsIndicator with name '" + name + "' already registered");
			}
		}
	}

	@Override
	public ToolsIndicator unregister(String name) {
		Assert.notNull(name, "Name must not be null");
		synchronized (this.monitor) {
			return this.toolsIndicators.remove(name);
		}
	}

	@Override
	public ToolsIndicator get(String name) {
		Assert.notNull(name, "Name must not be null");
		synchronized (this.monitor) {
			return this.toolsIndicators.get(name);
		}
	}

	@Override
	public Map<String, ToolsIndicator> getAll() {
		synchronized (this.monitor) {
			return Collections.unmodifiableMap(new LinkedHashMap<>(this.toolsIndicators));
		}
	}

}
