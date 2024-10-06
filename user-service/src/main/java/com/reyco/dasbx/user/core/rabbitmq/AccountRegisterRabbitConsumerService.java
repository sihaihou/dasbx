package com.reyco.dasbx.user.core.rabbitmq;

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
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.config.rabbitmq.service.AbstractRabbitConsumerService;
import com.reyco.dasbx.config.rabbitmq.service.RabbitMessageType;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.dto.SysMessageInsertDto;
import com.reyco.dasbx.model.msg.AccountRegisterMessage;
import com.reyco.dasbx.model.msg.RabbitMessage;
import com.reyco.dasbx.user.core.model.dto.SysAccountRegisterDto;
import com.reyco.dasbx.user.core.service.SysMessageService;
import com.reyco.dasbx.user.core.service.sys.SysAccountService;

@Service
public class AccountRegisterRabbitConsumerService extends AbstractRabbitConsumerService{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountRegisterRabbitConsumerService.class);
	
	@Autowired
	public SysAccountService  accountService;
	@Autowired
	private SysMessageService sysMessageService;
	
	@RabbitHandler
	@RabbitListener(bindings=@QueueBinding(
			value=@Queue(value=RabbitConstants.ACCOUNT_REGISTER_QUEUE,durable="true",exclusive="false",autoDelete="false"),
			exchange=@Exchange(value=RabbitConstants.ACCOUNT_EXCHANGE,type=ExchangeTypes.DIRECT,durable="true",autoDelete="false"),
			key=RabbitConstants.ACCOUNT_REGISTER_ROUTE_KEY)
	)
	public void register(AccountRegisterMessage accountRegisterMessage, Channel channel, Message message) {
		handler(accountRegisterMessage, channel, message);
	}

	@Override
	protected void doHandler(RabbitMessage rabbitMessage) throws Exception {
		AccountRegisterMessage accountRegisterMessage = (AccountRegisterMessage)rabbitMessage;
		SysAccountRegisterDto sysAccountRegisterDto = Convert.sourceToTarget(accountRegisterMessage, SysAccountRegisterDto.class);
		accountService.register(sysAccountRegisterDto);
	}

	@Override
	protected void handlerExceptionRabbitMessage(RabbitMessage rabbitMessage,Exception e) throws Exception {
		AccountRegisterMessage accountRegisterMessage = (AccountRegisterMessage)rabbitMessage;
		SysMessageInsertDto sysMessageInsertDto = new SysMessageInsertDto();
		sysMessageInsertDto.setType(1L);
		sysMessageInsertDto.setUserId(1L);
		sysMessageInsertDto.setBuzId(accountRegisterMessage.getUid());
		sysMessageInsertDto.setBuzType((byte)4);
		sysMessageInsertDto.setBuzName("注册账号失败");
		sysMessageInsertDto.setContent("注册账号失败,需要人工介入处理...");
		sysMessageInsertDto.setMetadata(JsonUtils.objToJson(accountRegisterMessage));
		sysMessageService.insert(sysMessageInsertDto);
	}

	@Override
	protected RabbitMessageType getRabbitMessageType() {
		return new RabbitMessageType() {
			@Override
			public byte getType() {
				return 4;
			}
			
			@Override
			public String getRetryKey() {
				return CachePrefixConstants.ACCOUNT_REGISTER_RETRY;
			}
		};
	}
	
}
