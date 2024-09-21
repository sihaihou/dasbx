package com.reyco.dasbx.config.rabbitmq.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.Channel;
import com.reyco.dasbx.config.redis.RedisUtil;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.msg.RabbitMessage;

public abstract class AbstractRabbitConsumerService implements RabbitConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractRabbitConsumerService.class);
	
	public static final String RABBIT_CORRELATIONDATA_ID_TYPE_PREFIX = "rabbit:correlationdata:id:type:";
	@Autowired
	protected RedisUtil redisUtil;
	
	@Override
	public void handler(RabbitMessage rabbitMessage, Channel channel, Message message) {
		RabbitMessageType rabbitMessageType = getRabbitMessageType();
		try {
			if(redisUtil.isMember(CachePrefixConstants.RABBIT_CORRELATIONDATA_ID_TYPE_PREFIX+rabbitMessageType.getType(),rabbitMessage.getCorrelationDataId())) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			doHandler(rabbitMessage);
			redisUtil.add(CachePrefixConstants.RABBIT_CORRELATIONDATA_ID_TYPE_PREFIX+rabbitMessageType.getType(),rabbitMessage.getCorrelationDataId());
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			logger.debug("【消息消费】消息消费成功,类型:{},消息{}",rabbitMessageType.getType(),rabbitMessage);
		} catch(Exception e) {
			logger.error("【消息异常处理】消息消费失败,出现异常,消息类型:{},消息详情:{},异常信息:{}", rabbitMessageType.getType(),rabbitMessage,e.getMessage());
			e.printStackTrace();
			handlerException(e, rabbitMessage, channel, message);
		} 
	}
	/**
	 * 异常处理
	 * @param exception
	 * @param rabbitMessage
	 * @param channel
	 * @param message
	 */
	public void handlerException(Exception e, RabbitMessage rabbitMessage, Channel channel, Message message) {
		RabbitMessageType rabbitMessageType = getRabbitMessageType();
		try {
			boolean hasKey = redisUtil.hasKey(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId());
			if(!hasKey) {
				redisUtil.put(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId(), 1+"");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			}
			String retry = redisUtil.hget(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId());
			Integer retries = Integer.parseInt(retry);
			if(retries<rabbitMessageType.getRetryTime()) {
				retries += 1;
				redisUtil.put(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId(),retries.toString());
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			}else {
				try {
					logger.info("【消息异常处理】消息消费失败,添加到人工处理消息,消息类型:{},消息详情:{}", rabbitMessageType.getType(),rabbitMessage);
					handlerExceptionRabbitMessage(rabbitMessage,e);
				} catch (Exception e1) {
					logger.error("【消息异常处理】消息消费失败,添加到人工处理消息失败,消息类型:{},消息详情:{},异常消息:{}",rabbitMessageType.getType(),rabbitMessage,e1.getMessage());
					e1.printStackTrace();
				}finally {
					redisUtil.delete(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId());
					channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
				}
			}
		} catch (IOException e1) {
			logger.error("【消息异常处理】消息消费失败,处理异常失败,消息类型:{},消息详情:{},异常消息:{}",rabbitMessageType.getType(),rabbitMessage,e1.getMessage());
			e1.printStackTrace();
		}
	}
	/**
	 * 业务处理，由开发者实现
	 * @param rabbitMessage
	 * @throws Exception
	 */
	protected abstract void doHandler(RabbitMessage rabbitMessage) throws Exception;
	/**
	 * 处理消息
	 * @param rabbitMessage
	 */
	protected abstract void handlerExceptionRabbitMessage(RabbitMessage rabbitMessage,Exception e) throws Exception;
	/**
	 * 获取类型
	 * @return
	 */
	protected abstract RabbitMessageType getRabbitMessageType();
}
