package com.reyco.dasbx.rabbitmq.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.rabbitmq.service.RabbitProducrService;
import com.reyco.dasbx.redis.auto.configuration.RedisAutoConfiguration;

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
@ConditionalOnProperty(name="reyco.dasbx.rabbitmq.enabled",matchIfMissing=true)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RabbitMQAutoConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQAutoConfiguration.class);
	
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
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(5);
        factory.setPrefetchCount(5);
        configurer.configure(factory, connectionFactory);
        return factory;
	}
	
	@Configuration
	public class RabbitProductServiceAutoConfiguration{
		
		@Bean
		@ConditionalOnMissingBean
		public RabbitProducrService rabbitProducrService(RabbitTemplate rabbitTemplate) {
			RabbitProducrService rabbitProducrService = new RabbitProducrService();
			rabbitProducrService.setRabbitTemplate(rabbitTemplate);
			return rabbitProducrService;
		}
		
	}
	
}
