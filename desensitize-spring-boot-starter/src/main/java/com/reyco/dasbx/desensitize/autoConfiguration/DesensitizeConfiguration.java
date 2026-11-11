package com.reyco.dasbx.desensitize.autoConfiguration;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.Module;
import com.reyco.dasbx.desensitize.MaskingEngine;
import com.reyco.dasbx.desensitize.core.DesensitizeJacksonModule;
import com.reyco.dasbx.desensitize.handler.DefaultExpressionDesensitizeHandler;
import com.reyco.dasbx.desensitize.handler.DelegateDesensitizeHandler;
import com.reyco.dasbx.desensitize.handler.DesensitizeHandler;
import com.reyco.dasbx.desensitize.handler.IndexDesensitizeHandler;
import com.reyco.dasbx.desensitize.log.LogMaskingEngine;
import com.reyco.dasbx.desensitize.log.LogMaskingEngineFactory;
import com.reyco.dasbx.desensitize.log.LogMaskingEngineRegistry;
import com.reyco.dasbx.desensitize.log.LogMaskingProperties;
import com.reyco.dasbx.desensitize.resolver.DesensitizeTypeResolver;

@Configuration
@ConditionalOnProperty(name = DesensitizeConfiguration.ENABLED, matchIfMissing = true)
@EnableConfigurationProperties(LogMaskingProperties.class)
public class DesensitizeConfiguration {

	public static final String ENABLED = "reyco.dasbx.desensitize.enabled";
	
	/**
	 * Jackson相关
	 * @param maskingEngine
	 * @param typeResolver
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(name = "desensitizeJacksonModule")
	public Module desensitizeJacksonModule(MaskingEngine maskingEngine, DesensitizeTypeResolver typeResolver) {
		return new DesensitizeJacksonModule(maskingEngine, typeResolver);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public MaskingEngine maskingEngine(DesensitizeHandler desensitizeHandler,DesensitizeTypeResolver desensitizeTypeResolver) {
		return new MaskingEngine(desensitizeHandler, desensitizeTypeResolver);
	}

	@Bean
	@ConditionalOnMissingBean
	public DesensitizeTypeResolver desensitizeTypeResolver() {
		return new DesensitizeTypeResolver();
	}
	
	@Bean
	@Primary
	public DesensitizeHandler delegateDesensiteHandler(List<DesensitizeHandler> desensitizeHandlers,DesensitizeHandler defaultDesensitizeHandler) {
		DelegateDesensitizeHandler delegateDesensiteHandler = new DelegateDesensitizeHandler();
		delegateDesensiteHandler.setDesensiteHandlers(desensitizeHandlers, defaultDesensitizeHandler);
		return delegateDesensiteHandler;
	}

	@Bean
	public DesensitizeHandler indexDesensitizeHandler() {
		return new IndexDesensitizeHandler();
	}

	@Bean("defaultDesensitizeHandler")
	public DesensitizeHandler defaultDesensitizeHandler() {
		return new DefaultExpressionDesensitizeHandler();
	}
	
	/**
	 * log相关
	 * @param properties
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = LogMaskingProperties.PREFIX + ".enabled", matchIfMissing = true)
	public LogMaskingEngine logMaskingEngine(LogMaskingProperties properties) {
		LogMaskingEngine maskingEngine = LogMaskingEngineFactory.create(properties);
		LogMaskingEngineRegistry.registry(maskingEngine);
		return maskingEngine;
	}

}
