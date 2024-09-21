package com.reyco.dasbx.config.rabbitmq.service;

import org.springframework.amqp.core.Message;

import com.rabbitmq.client.Channel;
import com.reyco.dasbx.model.msg.RabbitMessage;

/**
 * 消费者
 * @author reyco
 *
 */
public interface RabbitConsumerService{
	
	void handler(RabbitMessage rabbitMessage,Channel channel, Message message);
	
}
