package com.reyco.dasbx.config.sysLog;

import org.springframework.beans.BeanUtils;

import com.reyco.dasbx.config.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.log.core.DefaultSyslogMessage;
import com.reyco.dasbx.log.core.SyslogMessage;
import com.reyco.dasbx.log.core.handler.SyslogHandler;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.msg.SysLogRabbitmqMessage;

public class RabbitmqSyslogHandler implements SyslogHandler{
	
	private RabbitProducrService rabbitProducrService;
	
	public RabbitmqSyslogHandler() {
	}
	public RabbitmqSyslogHandler(RabbitProducrService rabbitProducrService) {
		super();
		this.rabbitProducrService = rabbitProducrService;
	}
	public void setRabbitProducrService(RabbitProducrService rabbitProducrService) {
		this.rabbitProducrService = rabbitProducrService;
	}
	@Override
	public void handler(SyslogMessage syslogMessage) {
		DefaultSyslogMessage defaultSyslogMessage = (DefaultSyslogMessage)syslogMessage;
		SysLogRabbitmqMessage rabbitMessage = new SysLogRabbitmqMessage();
		BeanUtils.copyProperties(defaultSyslogMessage, rabbitMessage);
		rabbitMessage.setCode(Long.parseLong(defaultSyslogMessage.getCode()));
		rabbitProducrService.send(RabbitConstants.LOG_EXCHANGE, RabbitConstants.LOG_SYS_ROUTE_KEY,rabbitMessage);
	}
	
}
