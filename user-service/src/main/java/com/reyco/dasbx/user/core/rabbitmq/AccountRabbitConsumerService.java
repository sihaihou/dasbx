package com.reyco.dasbx.user.core.rabbitmq;

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
import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.DateUtils;
import com.reyco.dasbx.model.constants.RabbitConstants;
import com.reyco.dasbx.model.msg.AccountRegisterMessage;
import com.reyco.dasbx.user.core.model.dto.SysAccountRegisterDto;
import com.reyco.dasbx.user.core.service.sys.SysAccountService;

@Service
public class AccountRabbitConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountRabbitConsumerService.class);
	
	@Autowired
	public SysAccountService  accountService;
	
	@RabbitHandler
	@RabbitListener(bindings=@QueueBinding(
			value=@Queue(value=RabbitConstants.ACCOUNT_REGISTER_QUEUE,durable="true",exclusive="false",autoDelete="false"),
			exchange=@Exchange(value=RabbitConstants.ACCOUNT_EXCHANGE,type=ExchangeTypes.DIRECT,durable="true",autoDelete="false"),
			key=RabbitConstants.ACCOUNT_REGISTER_ROUTE_KEY)
	)
	public void registAccount(AccountRegisterMessage accountRegisterMessage, Channel channel, Message message) {
		try {
			SysAccountRegisterDto sysAccountRegisterDto = Convert.sourceToTarget(accountRegisterMessage, SysAccountRegisterDto.class);
			accountService.register(sysAccountRegisterDto);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			logger.debug("【消息消费:注册账号】消息消费成功, {},{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),accountRegisterMessage);
		} catch (IOException e) {
			logger.error("【消息消费:注册账号】消息消费失败, {},{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),accountRegisterMessage);
		}
	}
	
	
}
