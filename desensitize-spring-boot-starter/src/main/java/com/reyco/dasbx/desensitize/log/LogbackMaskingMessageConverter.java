package com.reyco.dasbx.desensitize.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Logback 消息转换器，替换默认的 %m、%msg、%message 输出。
 */
public class LogbackMaskingMessageConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		String message = event.getFormattedMessage();
		LogMaskingEngine maskingEngine = LogMaskingEngineRegistry.get();
		return maskingEngine == null ? message : maskingEngine.mask(message);
	}
}
