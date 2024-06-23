package com.reyco.dasbx.common.core.rabbitmq;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.reyco.dasbx.common.core.model.dto.sys.SysLogInsertDto;
import com.reyco.dasbx.common.core.service.sys.SysLogService;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.DateUtils;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.msg.SysLogRabbitmqMessage;

@Service
public class SysLogRabbitConsumerService{

	private static final Logger logger = LoggerFactory.getLogger(SysLogRabbitConsumerService.class);

	@Autowired
	private SysLogService SysLogService;

	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = RabbitConstants.LOG_SYS_QUEUE, durable = "true", exclusive = "false", autoDelete = "false"), 
			exchange = @Exchange(value = RabbitConstants.LOG_EXCHANGE, type = ExchangeTypes.DIRECT, durable = "true", autoDelete = "false"), 
			key = RabbitConstants.LOG_SYS_ROUTE_KEY))
	public void log(SysLogRabbitmqMessage sysLogRabbitmqMessage, Channel channel, Message message) {
		try {
			SysLogInsertDto sysLogInsertDto = Convert.sourceToTarget(sysLogRabbitmqMessage, SysLogInsertDto.class);
			SysLogService.insert(sysLogInsertDto);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			logger.debug("【消息消费:系统日志】消息消费成功, {},{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),sysLogRabbitmqMessage);
		} catch (IOException e) {
			logger.error("【消息消费:系统日志】消息消费失败, {},{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),sysLogRabbitmqMessage);
			//根据业务重试...
		}
	}
}
