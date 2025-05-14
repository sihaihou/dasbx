package com.reyco.dasbx.resource.annotation;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.reyco.dasbx.resource.core.handler.CompositeResourceHandler;
import com.reyco.dasbx.resource.core.handler.ResourceHandler;
import com.reyco.dasbx.resource.core.handler.SimpleResourceHandler;


@Configuration
@AutoConfigureBefore(ResourceConfiguration.class)
public class ResourceHandlerConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(ResourceHandler.class)
	public ResourceHandler simpleResourceHandler() {
		return new SimpleResourceHandler();
	}
	
	@Bean
	@Primary
	public ResourceHandler compositeResourceHandler(List<ResourceHandler> resourceHandler) {
		return new CompositeResourceHandler(resourceHandler);
	}
}
