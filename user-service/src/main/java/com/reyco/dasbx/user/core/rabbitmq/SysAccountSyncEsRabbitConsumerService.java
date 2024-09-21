package com.reyco.dasbx.user.core.rabbitmq;

import javax.annotation.Resource;

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
import com.reyco.dasbx.config.es.sync.SyncElasticsearchService;
import com.reyco.dasbx.config.rabbitmq.service.AbstractRabbitConsumerService;
import com.reyco.dasbx.config.rabbitmq.service.RabbitMessageType;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.OperationType;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;
import com.reyco.dasbx.model.msg.RabbitMessage;
import com.reyco.dasbx.model.msg.SysAccountSyncEsMessage;
import com.reyco.dasbx.user.core.service.SysMessageService;

@Service
public class SysAccountSyncEsRabbitConsumerService extends AbstractRabbitConsumerService{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountRegisterRabbitConsumerService.class);
	
	@Autowired
	private SysMessageService sysMessageService;
	
	@Resource(name="sysAccountSyncElasticsearchService")
	private SyncElasticsearchService syncElasticsearchService;
	
	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = RabbitConstants.ACCOUNT_SYNC_ES_QUEUE, durable = "true", exclusive = "false", autoDelete = "false"), 
			exchange = @Exchange(value = RabbitConstants.ACCOUNT_EXCHANGE, type = ExchangeTypes.DIRECT, durable = "true", autoDelete = "false"), 
			key = RabbitConstants.ACCOUNT_SYNC_ES_ROUTE_KEY))
	public void syncElasticsearch(SysAccountSyncEsMessage sysAccountSyncEsMessage, Channel channel, Message message) {
		handler(sysAccountSyncEsMessage, channel, message);
	}

	@Override
	protected void doHandler(RabbitMessage rabbitMessage) throws Exception {
		SysAccountSyncEsMessage sysAccountSyncEsMessage = (SysAccountSyncEsMessage)rabbitMessage;
		OperationType type = sysAccountSyncEsMessage.getType();
		if(OperationType.CREATE==type || OperationType.UPDATE==type) {
			syncElasticsearchService.syncElasticsearch(sysAccountSyncEsMessage.getAccountId());
		}else if(OperationType.DELETE==type) {
			syncElasticsearchService.syncDeleteElasticsearch(sysAccountSyncEsMessage.getAccountId());
		}
	}

	@Override
	protected void handlerExceptionRabbitMessage(RabbitMessage rabbitMessage,Exception e) throws Exception {
		SysMessageInsertDto sysMessageInsertDto = new SysMessageInsertDto();
		sysMessageInsertDto.setType(1L);
		sysMessageInsertDto.setUserId(1L);
		sysMessageInsertDto.setBuzId(rabbitMessage.getCorrelationDataId());
		sysMessageInsertDto.setBuzType((byte)5);
		sysMessageInsertDto.setBuzName("账号同步ES失败");
		sysMessageInsertDto.setContent("账号同步ES失败,需要人工介入处理...");
		sysMessageService.insert(sysMessageInsertDto);
	}

	@Override
	protected RabbitMessageType getRabbitMessageType() {
		return new RabbitMessageType() {
			@Override
			public byte getType() {
				return 5;
			}
			@Override
			public String getRetryKey() {
				return CachePrefixConstants.SYNC_ES_ACCOUNT_RETRY;
			}
		};
	}
}
