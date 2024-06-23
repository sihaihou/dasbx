package com.reyco.dasbx.config.sysLog;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.config.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.log.core.handler.SyslogHandler;

@Configuration
@ConditionalOnBean(value= {RabbitProducrService.class})
@ConditionalOnClass(value = {SyslogHandler.class })
public class RabbitmqSyslogHandlerConfiguration {
	
	@Bean
	public SyslogHandler rabbitmqSyslogHandler(RabbitProducrService rabbitProducrService) {
		return new RabbitmqSyslogHandler(rabbitProducrService);
	}
}
