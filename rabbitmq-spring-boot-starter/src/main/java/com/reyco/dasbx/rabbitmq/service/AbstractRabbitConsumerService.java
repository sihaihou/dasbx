package com.reyco.dasbx.rabbitmq.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import com.rabbitmq.client.Channel;
import com.reyco.dasbx.rabbitmq.model.RabbitMessage;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;

public abstract class AbstractRabbitConsumerService implements RabbitConsumerService {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractRabbitConsumerService.class);
	
	public static final String RABBIT_CORRELATIONDATA_ID_TYPE_PREFIX = "rabbit:correlationdata:id:type:";
	
	protected RedisUtil redisUtil;
	
	public AbstractRabbitConsumerService(RedisUtil redisUtil) {
		super();
		this.redisUtil = redisUtil;
	}
	
	@Override
	public void handler(RabbitMessage rabbitMessage, Channel channel, Message message) {
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		RabbitMessageType rabbitMessageType = getRabbitMessageType();
		
		String setKey = AbstractRabbitConsumerService.RABBIT_CORRELATIONDATA_ID_TYPE_PREFIX+rabbitMessageType.getType();
		String msgId = rabbitMessage.getCorrelationDataId();
		try {
			// 1. 判断是否重复消费过
			if(redisUtil.isMember(setKey,msgId)) {
				logger.warn("【消息消费】检测到重复消息，直接拦截丢弃。类型:{},ID: {},消息详情:{}",rabbitMessageType.getType(),rabbitMessage, rabbitMessage.getCorrelationDataId());
				channel.basicAck(deliveryTag, false);
				return;
			}
			// 2. 执行真正的业务
			doHandler(rabbitMessage);
			// 3. 业务成功，塞入你的 Redis Set 集合中持久化防重
			redisUtil.add(setKey,msgId);
			
			channel.basicAck(deliveryTag, false);
			logger.debug("【消息消费】消息消费成功,类型:{},消息{}",rabbitMessageType.getType(),rabbitMessage);
		} catch(Exception e) {
			logger.error("【消息异常处理】消息消费失败,出现异常,消息类型:{},消息详情:{},异常信息:{}", rabbitMessageType.getType(),rabbitMessage,e.getMessage());
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
		long deliveryTag = message.getMessageProperties().getDeliveryTag();
		RabbitMessageType rabbitMessageType = getRabbitMessageType();

		String msgId = rabbitMessage.getCorrelationDataId();
		String retryKey = rabbitMessageType.getRetryKey();
		try {
			boolean hasKey = redisUtil.hasKey(retryKey,msgId);
			if(!hasKey) {
				// 第一次失败：初始化重试次数为 1
				redisUtil.put(retryKey, msgId, "1");
				logger.warn("【消息重试】消费发生异常，开始第 1 次重试。消息类型:{},ID: {},消息详情:{}",rabbitMessageType.getType(), msgId,rabbitMessage);
				channel.basicNack(deliveryTag, false, true);
				return;
			}
			// 已经重试过了，读取并自增次数
			String retry = redisUtil.hget(retryKey, msgId);
			Integer retries = Integer.parseInt(retry);
			
			if(retries<rabbitMessageType.getRetryTime()) {
				retries += 1;
				redisUtil.put(retryKey, msgId, retries.toString());
				logger.warn("【消息重试】消费发生异常，消息类型:{},开始第 {} 次重试,ID: {},消息详情:{}", rabbitMessageType.getType(),retries, msgId,rabbitMessage);
				channel.basicNack(deliveryTag, false, true);
			}else {
				try {
					logger.info("【消息异常处理】消息重试耗尽，开始推进人工介入留痕池...,消息类型:{},ID: {},消息详情:{}", rabbitMessageType.getType(),msgId,rabbitMessage);
					handlerExceptionRabbitMessage(rabbitMessage,e);
				} catch (Exception e1) {
					logger.error("【消息异常处理】严重错误：推进人工留痕池也失败了！消息类型:{},ID: {},消息详情:{},异常消息:{}",rabbitMessageType.getType(),msgId,rabbitMessage,e1.getMessage());
					e.printStackTrace();
				}finally {
					redisUtil.delete(retryKey, msgId);
					channel.basicNack(deliveryTag, false, false);
				}
			}
		} catch (IOException e1) {
			logger.error("【消息异常处理】消息消费失败,处理异常失败,消息类型:{},消息详情:{},异常消息:{}",rabbitMessageType.getType(),rabbitMessage,e1.getMessage());
			e.printStackTrace();
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
