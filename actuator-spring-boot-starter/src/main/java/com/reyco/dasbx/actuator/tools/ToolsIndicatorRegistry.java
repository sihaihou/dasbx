package com.reyco.dasbx.actuator.tools;

import java.util.Map;

public interface ToolsIndicatorRegistry {
	
	void register(String name, ToolsIndicator toolsIndicator);

	ToolsIndicator unregister(String name);
	
	ToolsIndicator get(String name);
	
	Map<String, ToolsIndicator> getAll();

}
