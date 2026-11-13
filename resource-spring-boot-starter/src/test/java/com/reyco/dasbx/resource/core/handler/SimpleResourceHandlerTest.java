package com.reyco.dasbx.resource.core.handler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.resource.core.model.DefaultResourceDefinition;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class SimpleResourceHandlerTest {

	@Test
	public void logsResourceDefinitionWhenDebugEnabled() {
		Logger logger = (Logger) LoggerFactory.getLogger(SimpleResourceHandler.class);
		Level originalLevel = logger.getLevel();

		ListAppender<ILoggingEvent> appender = new ListAppender<ILoggingEvent>();
		appender.start();
		logger.addAppender(appender);
		logger.setLevel(Level.DEBUG);

		try {
			DefaultResourceDefinition definition = new DefaultResourceDefinition();
			definition.setApplication("user-service");
			definition.setResource("/user/1");
			definition.setMethod("GET");
			definition.setDescription("query user");
			definition.setToken("******");
			definition.setSuccess(true);
			definition.setStartTime(1000L);
			definition.setEndTime(1100L);

			new SimpleResourceHandler().handler(definition);

			assertFalse(appender.list.isEmpty());
			assertTrue(appender.list.get(0).getFormattedMessage().contains("/user/1"));
		} finally {
			logger.detachAppender(appender);
			logger.setLevel(originalLevel);
			appender.stop();
		}
	}
}
