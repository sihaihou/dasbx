package com.reyco.dasbx.actuator.autoconfigure.tools;

import java.util.Map;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.actuator.tools.ToolsIndicator;
import com.reyco.dasbx.actuator.tools.ToolsIndicatorRegistry;
import com.reyco.dasbx.actuator.tools.ToolsIndicatorRegistryFactory;

@Configuration
@AutoConfigureBefore(ToolsEndpointConfiguration.class)
public class ToolsIndicatorAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ToolsIndicatorRegistry.class)
	public ToolsIndicatorRegistry toolsIndicatorRegistry(ApplicationContext applicationContext) {
		Map<String, ToolsIndicator> toolsIndicators = applicationContext.getBeansOfType(ToolsIndicator.class);
		ToolsIndicatorRegistryFactory factory = new ToolsIndicatorRegistryFactory();
		return factory.createToolsIndicatorRegistry(toolsIndicators);
	}
}
