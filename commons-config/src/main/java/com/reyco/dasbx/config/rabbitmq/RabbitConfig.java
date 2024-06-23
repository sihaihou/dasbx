package com.reyco.dasbx.config.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基本概念： 
 * queue：队列，每个队列可以有多个消费者，但是一条消息只会被一个消费者消费
 * exchange:交换机，队列可以绑定交换机，交换机根据路由或者其他匹配信息将消息发送至queue 
 * 模式介绍
 * simple模式：不需要交换机，直连模式。一个队列只有一个消费者;
 * work模式：一个队列多个消费者;
 * direct模式：需要交换机，通过交换机的路由key，精确匹配queue，并发送至对应的queue;
 * topic模式：通过路由与路由key，模糊匹配的模式。可用通配符。比如key.1会被绑定路由key.*的queue获取到;
 * fanout模式: 广播模式，不需要路由key，给所有绑定到交换机的queue.
 * 
 * @author reyco
 *
 */
@Configuration
public class RabbitConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	    RabbitTemplate rabbitTemplate = new RabbitTemplate();
	    rabbitTemplate.setConnectionFactory(connectionFactory);
	    rabbitTemplate.setMandatory(true);
	    rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause)
            {
            	if(ack) {
    				logger.debug("【发送消息】消息发送成功:"+correlationData.getId());
    			}else {
    				logger.error("【发送消息】确认消息送到交换机(Exchange)结果：");
                	logger.error("【发送消息】相关数据：" + correlationData);
                	logger.error("【发送消息】是否成功：" + ack);
                	logger.error("【发送消息】错误原因：" + cause);
    			}
            }
        });
	    //确认消息送到队列(Queue)回调
	    rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
				logger.warn("\n确认消息送到队列(Queue)结果：");
				logger.warn("发生消息：" + message);
				logger.warn("回应码：" + replyCode);
				logger.warn("回应信息：" + replyText);
				logger.warn("交换机：" + exchange);
				logger.warn("路由键：" +routingKey);
			}
		});
	    return rabbitTemplate;
	}
	/**
	 * 注册账号
	 * @return
	 */
	/*@Bean
	public DirectExchange accountExchange() {
		return new DirectExchange(RabbitConstants.ACCOUNT_EXCHANGE, true, false);
	}
	@Bean
	public Queue accountRegisterQueue() {
		return new Queue(RabbitConstants.ACCOUNT_REGISTER_QUEUE, true, false, false);
	}
	@Bean
	public Binding accountRegisterQueueDeadBinding() {
		return BindingBuilder.bind(accountRegisterQueue()).to(accountExchange()).with(RabbitConstants.ACCOUNT_REGISTER_ROUTE_KEY);
	}*/
	
	
	/**
	 * 日志
	 * @return
	 *//*
	@Bean
	public DirectExchange logExchange() {
		return new DirectExchange(RabbitConstants.LOG_EXCHANGE, true, false);
	}*/
	//登录
	/*@Bean
	public Queue logLoginQueue() {
		return new Queue(RabbitConstants.LOG_LOGIN_QUEUE, true, false, false);
	}
	@Bean
	public Binding logLoginQueueDeadBinding() {
		return BindingBuilder.bind(logLoginQueue()).to(logExchange()).with(RabbitConstants.LOG_LOGIN_ROUTE_KEY);
	}*/
	//登出
	/*@Bean
	public Queue logLogoutQueue() {
		return new Queue(RabbitConstants.LOG_LOGOUT_QUEUE, true, false, false);
	}
	@Bean
	public Binding logLogoutQueueDeadBinding() {
		return BindingBuilder.bind(logLogoutQueue()).to(logExchange()).with(RabbitConstants.LOG_LOGOUT_ROUTE_KEY);
	}*/
	//系统日志
	/*@Bean
	public Queue logSysQueue() {
		return new Queue(RabbitConstants.LOG_SYS_QUEUE, true, false, false);
	}
	@Bean
	public Binding logSysQueueDeadBinding() {
		return BindingBuilder.bind(logSysQueue()).to(logExchange()).with(RabbitConstants.LOG_SYS_ROUTE_KEY);
	}*/
}
