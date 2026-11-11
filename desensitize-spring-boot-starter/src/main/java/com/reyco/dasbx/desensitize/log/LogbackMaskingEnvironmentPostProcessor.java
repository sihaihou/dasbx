package com.reyco.dasbx.desensitize.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import ch.qos.logback.classic.PatternLayout;

/**
 * 在 Logback 创建 PatternLayout 之前接管默认消息转换词。
 */
public class LogbackMaskingEnvironmentPostProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		PatternLayout.defaultConverterMap.put("m", LogbackMaskingMessageConverter.class.getName());
		PatternLayout.defaultConverterMap.put("msg", LogbackMaskingMessageConverter.class.getName());
		PatternLayout.defaultConverterMap.put("message", LogbackMaskingMessageConverter.class.getName());

		boolean desensitizeEnabled = environment.getProperty("reyco.dasbx.desensitize.enabled", Boolean.class, true);
		boolean logMaskingEnabled = environment.getProperty(LogMaskingProperties.PREFIX + ".enabled", Boolean.class,
				true);

		if (!desensitizeEnabled || !logMaskingEnabled) {
			LogMaskingEngineRegistry.clear();
			return;
		}

		LogMaskingProperties properties = Binder.get(environment)
				.bind(LogMaskingProperties.PREFIX, Bindable.of(LogMaskingProperties.class))
				.orElse(new LogMaskingProperties());

		LogMaskingEngineRegistry.registry(LogMaskingEngineFactory.create(properties));
	}
}
