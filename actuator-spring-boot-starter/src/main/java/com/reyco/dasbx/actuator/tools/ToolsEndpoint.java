package com.reyco.dasbx.actuator.tools;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.util.Assert;

@Endpoint(id = "tools")
public class ToolsEndpoint {

	private ToolsIndicator toolsIndicator;

	public ToolsEndpoint() {
	}

	public ToolsEndpoint(ToolsIndicator toolsIndicator) {
		Assert.notNull(toolsIndicator, "toolsIndicator must not be null");
		this.toolsIndicator = toolsIndicator;
	}

	@ReadOperation
	public Tools tools() {
		return this.toolsIndicator.tools();
	}
	/**
	 * http://host:port/actuator/tools/ignored?component=jwt
	 * @param component
	 * @return
	 */
	@ReadOperation
	public Tools toolsForComponent(@Selector String component) {
		ToolsIndicator indicator = getNestedToolsIndicator(this.toolsIndicator, component);
		return (indicator != null) ? indicator.tools() : null;
	}

	private ToolsIndicator getNestedToolsIndicator(ToolsIndicator toolsIndicator, String name) {
		if (toolsIndicator instanceof CompositeToolsIndicator) {
			return ((CompositeToolsIndicator) toolsIndicator).getRegistry().get(name);
		}
		return null;
	}
}
