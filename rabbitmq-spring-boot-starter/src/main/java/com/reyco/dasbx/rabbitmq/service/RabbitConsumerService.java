package com.reyco.dasbx.rabbitmq.service;

import org.springframework.amqp.core.Message;

import com.rabbitmq.client.Channel;
import com.reyco.dasbx.rabbitmq.model.RabbitMessage;

/**
 * 消费者
 * @author reyco
 *
 */
public interface RabbitConsumerService{
	
	void handler(RabbitMessage rabbitMessage,Channel channel, Message message);
	
}
