package com.reyco.dasbx.actuator.autoconfigure.tools;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.actuator.tools.CompositeToolsIndicator;
import com.reyco.dasbx.actuator.tools.ToolsEndpoint;
import com.reyco.dasbx.actuator.tools.ToolsIndicatorRegistry;

@Configuration
@ConditionalOnSingleCandidate(ToolsIndicatorRegistry.class)
@ConditionalOnEnabledEndpoint(endpoint = ToolsEndpoint.class)
@AutoConfigureAfter(ToolsIndicatorAutoConfiguration.class)
public class ToolsEndpointConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ToolsEndpoint.class)
	public ToolsEndpoint toolsEndpoint(ToolsIndicatorRegistry registry) {
		return new ToolsEndpoint(new CompositeToolsIndicator(registry));
	}
	
}
