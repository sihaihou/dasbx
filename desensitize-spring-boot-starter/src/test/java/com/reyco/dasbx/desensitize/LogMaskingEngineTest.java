package com.reyco.dasbx.desensitize;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.reyco.dasbx.desensitize.autoConfiguration.DesensitizeConfiguration;
import com.reyco.dasbx.desensitize.log.LogMaskingEngine;
import com.reyco.dasbx.desensitize.log.LogMaskingEngineFactory;
import com.reyco.dasbx.desensitize.log.LogMaskingEngineRegistry;
import com.reyco.dasbx.desensitize.log.LogMaskingProperties;
import com.reyco.dasbx.desensitize.log.LogMaskingRule;
import com.reyco.dasbx.desensitize.log.LogbackMaskingMessageConverter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggingEvent;

public class LogMaskingEngineTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(DesensitizeConfiguration.class));

	@After
	public void clearRegistry() {
		LogMaskingEngineRegistry.clear();
	}

	@Test
	public void exposesLogMaskingEngineAsSpringBean() {
		contextRunner.run(context -> {
			assertThat(context).hasNotFailed();
			assertThat(context).hasSingleBean(LogMaskingEngine.class);
		});
	}

	@Test
	public void masksPhoneIdCardAndEmailInFreeText() {
		LogMaskingEngine engine = new LogMaskingEngine();

		assertThat(engine.mask(
				"login phone=13800138000 idCard=110101199001011234 email=alice@example.com"))
				.isEqualTo(
						"login phone=138****8000 idCard=110***********1234 email=a***@example.com");
	}

	@Test
	public void usesConfiguredRules() {
		LogMaskingProperties properties = new LogMaskingProperties();
		LogMaskingProperties.Rule rule = new LogMaskingProperties.Rule();
		rule.setName("token");
		rule.setRegex("token=([A-Za-z0-9]+)");
		rule.setReplacement("token=***");
		properties.setRules(Arrays.asList(rule));

		LogMaskingEngine engine = LogMaskingEngineFactory.create(properties);

		assertThat(engine.mask("request token=abcdefgh"))
				.isEqualTo("request token=***");
	}

	@Test
	public void masksWhenAtLeastOneRuleMatches() {
		LogMaskingEngine engine = new LogMaskingEngine(Arrays.asList(
				new LogMaskingRule("email", "\\S+@\\S+\\.\\S+", "***"),
				new LogMaskingRule("phone", "1\\d{10}", "***")));

		assertThat(engine.mask("contact a@example.com or 13800138000"))
				.isEqualTo("contact *** or ***");
	}

	@Test
	public void logbackConverterUsesConfiguredEngine() {
		LogMaskingEngineRegistry.registry(new LogMaskingEngine());

		LoggerContext context = new LoggerContext();
		Logger logger = context.getLogger("test");
		LoggingEvent event = new LoggingEvent(
				getClass().getName(),
				logger,
				Level.INFO,
				"phone={}",
				null,
				new Object[] { "13800138000" });

		LogbackMaskingMessageConverter converter = new LogbackMaskingMessageConverter();
		converter.setContext(context);

		assertThat(converter.convert(event)).isEqualTo("phone=138****8000");
	}
}
