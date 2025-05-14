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
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.OperationType;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;
import com.reyco.dasbx.rabbitmq.model.RabbitMessage;
import com.reyco.dasbx.rabbitmq.service.AbstractRabbitConsumerService;
import com.reyco.dasbx.rabbitmq.service.RabbitMessageType;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;
import com.reyco.dasbx.sync.es.ElasticsearchSync;
import com.reyco.dasbx.user.core.model.msg.SysAccountSyncEsMessage;
import com.reyco.dasbx.user.core.service.SysMessageService;
import com.reyco.dasbx.user.core.service.es.sysAccount.SysAccountSyncElasticsearchServiceImpl;

@Service
public class SysAccountSyncEsRabbitConsumerService extends AbstractRabbitConsumerService{

	private static final Logger logger = LoggerFactory.getLogger(AccountRegisterRabbitConsumerService.class);
	
	@Autowired
	private SysMessageService sysMessageService;
	
	@Resource(name=SysAccountSyncElasticsearchServiceImpl.SYNC_NAME)
	private ElasticsearchSync<Long,Integer> elasticsearchSync;
	
	@Autowired
	public SysAccountSyncEsRabbitConsumerService(RedisUtil redisUtil) {
		super(redisUtil);
	}
	
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
			elasticsearchSync.incrementUpdateSync(()->sysAccountSyncEsMessage.getAccountId());
		}else if(OperationType.DELETE==type) {
			elasticsearchSync.incrementDeleteSync(()->sysAccountSyncEsMessage.getAccountId());
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
