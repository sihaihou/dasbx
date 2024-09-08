package com.reyco.dasbx.common.core.rabbitmq;

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
import com.reyco.dasbx.common.core.service.sys.SysMessageService;
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.config.rabbitmq.service.AbstractRabbitConsumerService;
import com.reyco.dasbx.config.rabbitmq.service.RabbitMessageType;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;
import com.reyco.dasbx.model.msg.RabbitMessage;
import com.reyco.dasbx.model.msg.SysLogRabbitmqMessage;

@Service
public class SysLogRabbitConsumer extends AbstractRabbitConsumerService{

	private static final Logger logger = LoggerFactory.getLogger(SysLogRabbitConsumer.class);
	
	@Autowired
	private SysLogService SysLogService;
	
	@Autowired
	private SysMessageService sysMessageService;
	
	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(
			exchange = @Exchange(value = RabbitConstants.LOG_EXCHANGE, type = ExchangeTypes.DIRECT, durable = "true", autoDelete = "false"), 
			value = @Queue(value = RabbitConstants.LOG_SYS_QUEUE, durable = "true", exclusive = "false", autoDelete = "false"), 
			key = RabbitConstants.LOG_SYS_ROUTE_KEY),containerFactory="rabbitListenerContainerFactory")
	public void sysLog(SysLogRabbitmqMessage sysLogRabbitmqMessage, Channel channel, Message message) {
		execute(sysLogRabbitmqMessage, channel, message);
	}

	@Override
	protected void handler(RabbitMessage rabbitMessage) throws Exception {
		SysLogRabbitmqMessage sysLogRabbitmqMessage = (SysLogRabbitmqMessage)rabbitMessage;
		SysLogInsertDto sysLogInsertDto = Convert.sourceToTarget(sysLogRabbitmqMessage, SysLogInsertDto.class);
		SysLogService.insert(sysLogInsertDto);
	}
	
	@Override
	protected void handlerExceptionRabbitMessage(RabbitMessage rabbitMessage,Exception e) throws Exception {
		SysMessageInsertDto sysMessageInsertDto = new SysMessageInsertDto();
		sysMessageInsertDto.setType(1L);
		sysMessageInsertDto.setUserId(1L);
		sysMessageInsertDto.setBuzId(rabbitMessage.getCorrelationDataId());
		sysMessageInsertDto.setBuzType((byte)6);
		sysMessageInsertDto.setBuzName("系统日志新增失败");
		sysMessageInsertDto.setContent("系统日志新增失败,需要人工介入处理...");
		sysMessageInsertDto.setMetadata(JsonUtils.objToJson(rabbitMessage));
		sysMessageService.insert(sysMessageInsertDto);
	}
	
	@Override
	protected RabbitMessageType getRabbitMessageType() {
		RabbitMessageType rabbitMessageType = new RabbitMessageType() {
			@Override
			public byte getType() {
				return 6;
			}
			@Override
			public String getRetryKey() {
				return CachePrefixConstants.SYS_LOG_RETRY;
			}
		};
		return rabbitMessageType;
	}


}


