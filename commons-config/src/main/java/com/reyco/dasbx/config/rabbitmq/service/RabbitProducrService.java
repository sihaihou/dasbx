package com.reyco.dasbx.config.rabbitmq.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.model.msg.RabbitMessage;

@Component
public class RabbitProducrService{

	//private static final Logger logger = LoggerFactory.getLogger(RabbitProducrService.class);
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send(String exchange, String routeKey,RabbitMessage rabbitMessage) {
		MessagePostProcessor processor = new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				return message;
			}
		};
		rabbitTemplate.convertAndSend(exchange, routeKey, rabbitMessage,processor,new CorrelationData(rabbitMessage.getCorrelationDataId()));
	}
	
	/*public void send(String exchange, String routeKey,RabbitMessage rabbitMessage) {
		MessagePostProcessor processor = new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				logger.debug("message:"+JsonUtils.objToJson(message));
				return message;
			}
		};
		rabbitTemplate.setConfirmCallback((correlationData,ack,cause)->{
			if(ack) {
				logger.info("【发送消息】消息发送成功:"+rabbitMessage);
			}else {
				logger.info("【发送消息】消息发送失败:msg:{},cause by:{}"+rabbitMessage,cause);
			}
		});
		rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchangeName,routingKey)->{
			logger.info("返回消息回调:{} 应答代码:{} 回复文本:{} 交换器:{} 路由键:{}", message, replyCode, replyText, exchangeName, routingKey);
		});
		rabbitTemplate.convertAndSend(exchange, routeKey, rabbitMessage, processor,new CorrelationData(rabbitMessage.getCorrelationDataId().toString()));
	}*/
}
