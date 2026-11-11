package com.reyco.dasbx.desensitize;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import com.reyco.dasbx.desensitize.log.LogMaskingEngineRegistry;
import com.reyco.dasbx.desensitize.log.LogbackMaskingEnvironmentPostProcessor;
import com.reyco.dasbx.desensitize.log.LogbackMaskingMessageConverter;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;

public class LogbackMaskingEnvironmentPostProcessorTest {

	@After
	public void clearRegistry() {
		LogMaskingEngineRegistry.clear();
	}

	@Test
	public void installsConverterAndConfiguredRulesBeforeLogbackStarts() {
		MockEnvironment environment = new MockEnvironment()
				.withProperty("reyco.dasbx.desensitize.log.rules[0].name", "phone")
				.withProperty("reyco.dasbx.desensitize.log.rules[0].regex", "1\\d{10}")
				.withProperty("reyco.dasbx.desensitize.log.rules[0].replacement", "***");

		new LogbackMaskingEnvironmentPostProcessor()
				.postProcessEnvironment(environment, null);

		assertThat(PatternLayout.defaultConverterMap.get("m"))
				.isEqualTo(LogbackMaskingMessageConverter.class.getName());
		assertThat(PatternLayout.defaultConverterMap.get("msg"))
				.isEqualTo(LogbackMaskingMessageConverter.class.getName());
		assertThat(PatternLayout.defaultConverterMap.get("message"))
				.isEqualTo(LogbackMaskingMessageConverter.class.getName());
		assertThat(LogMaskingEngineRegistry.get().mask("phone=13800138000"))
				.isEqualTo("phone=***");
	}

	@Test
	public void patternLayoutMessageIsMaskedAutomatically() {
		MockEnvironment environment = new MockEnvironment()
				.withProperty("reyco.dasbx.desensitize.log.rules[0].name", "phone")
				.withProperty("reyco.dasbx.desensitize.log.rules[0].regex", "1\\d{10}")
				.withProperty("reyco.dasbx.desensitize.log.rules[0].replacement", "***");
		new LogbackMaskingEnvironmentPostProcessor()
				.postProcessEnvironment(environment, null);

		LoggerContext loggerContext = new LoggerContext();
		Logger logger = loggerContext.getLogger("test");
		LoggingEvent event = new LoggingEvent(
				getClass().getName(),
				logger,
				Level.INFO,
				"phone={}",
				null,
				new Object[] { "13800138000" });
		PatternLayout layout = new PatternLayout();
		layout.setContext(loggerContext);
		layout.setPattern("%msg");
		layout.start();

		String maskedMessage = layout.doLayout(event);
		System.out.println("========== LOGBACK MASKED MESSAGE ==========");
		System.out.println(maskedMessage);

		assertThat(maskedMessage).isEqualTo("phone=***");
	}
}
