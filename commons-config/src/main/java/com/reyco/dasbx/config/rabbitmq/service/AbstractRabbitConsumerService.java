package com.reyco.dasbx.config.rabbitmq.service;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.Channel;
import com.reyco.dasbx.commons.utils.DateUtils;
import com.reyco.dasbx.config.redis.RedisUtil;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.model.msg.RabbitMessage;

public abstract class AbstractRabbitConsumerService implements RabbitConsumerService {
	private static final Logger logger = LoggerFactory.getLogger(AbstractRabbitConsumerService.class);
	
	@Autowired
	protected RedisUtil redisUtil;
	
	@Override
	public void execute(RabbitMessage rabbitMessage, Channel channel, Message message) {
		try {
			RabbitMessageType rabbitMessageType = getRabbitMessageType();
			if(redisUtil.isMember(CachePrefixConstants.RABBIT_CORRELATIONDATA_ID_TYPE_PREFIX+rabbitMessageType.getType(),rabbitMessage.getCorrelationDataId())) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				return;
			}
			handler(rabbitMessage);
			redisUtil.add(CachePrefixConstants.RABBIT_CORRELATIONDATA_ID_TYPE_PREFIX+rabbitMessageType.getType(),rabbitMessage.getCorrelationDataId());
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			logger.debug("【消息消费】消息消费成功,时间 {},类型:{},消息{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),rabbitMessageType.getType(),rabbitMessage);
		} catch(Exception e) {
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
	public void handlerException(Exception exception, RabbitMessage rabbitMessage, Channel channel, Message message) {
		RabbitMessageType rabbitMessageType = getRabbitMessageType();
		try {
			logger.error("【消息异常处理】消息消费失败, 时间:{},消息类型:{},消息详情:{}", DateUtils.format(new Date(),DateUtils.DATE_TIME_FORMAT),rabbitMessageType.getType(),rabbitMessage);
			boolean hasKey = redisUtil.hasKey(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId());
			if(!hasKey) {
				redisUtil.put(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId(), 1+"");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			}
			String retry = redisUtil.hget(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId());
			Integer retries = Integer.parseInt(retry);
			if(retries<rabbitMessageType.getRetryTime()) {
				retries += retries;
				redisUtil.put(rabbitMessageType.getRetryKey(), rabbitMessage.getCorrelationDataId(),retries.toString());
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			}else {
				try {
					handlerRabbitMessage(rabbitMessage);
				} catch (Exception e1) {
					e1.printStackTrace();
				}finally {
					channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * 业务处理，由开发者实现
	 * @param rabbitMessage
	 * @throws Exception
	 */
	protected abstract void handler(RabbitMessage rabbitMessage) throws Exception;
	/**
	 * 处理消息
	 * @param rabbitMessage
	 */
	protected abstract void handlerRabbitMessage(RabbitMessage rabbitMessage) throws Exception;
	/**
	 * 获取类型
	 * @return
	 */
	protected abstract RabbitMessageType getRabbitMessageType();
}
