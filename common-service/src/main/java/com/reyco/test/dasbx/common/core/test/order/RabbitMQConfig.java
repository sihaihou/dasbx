package com.reyco.test.dasbx.common.core.test.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    // 订单队列
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.create";
    
    // 死信队列配置
    public static final String ORDER_DLQ = "order.dlq";
    public static final String ORDER_DLX = "order.dlx";
    
    // 订单支付队列
    public static final String ORDER_PAY_QUEUE = "order.pay.queue";
    public static final String ORDER_PAY_EXCHANGE = "order.pay.exchange";
    public static final String ORDER_PAY_ROUTING_KEY = "order.pay";
    
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }
    
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_DLX)
                .withArgument("x-dead-letter-routing-key", ORDER_DLQ)
                .withArgument("x-message-ttl", 60000) // 1分钟TTL
                .build();
    }
    
    @Bean
    public Queue orderDeadLetterQueue() {
        return new Queue(ORDER_DLQ, true);
    }
    
    @Bean
    public DirectExchange orderDeadLetterExchange() {
        return new DirectExchange(ORDER_DLX);
    }
    
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }
    
    @Bean
    public Binding orderDLQBinding() {
        return BindingBuilder.bind(orderDeadLetterQueue())
                .to(orderDeadLetterExchange())
                .with(ORDER_DLQ);
    }
    
    // 支付相关队列
    @Bean
    public DirectExchange orderPayExchange() {
        return new DirectExchange(ORDER_PAY_EXCHANGE);
    }
    
    @Bean
    public Queue orderPayQueue() {
        return new Queue(ORDER_PAY_QUEUE, true);
    }
    
    @Bean
    public Binding orderPayBinding() {
        return BindingBuilder.bind(orderPayQueue())
                .to(orderPayExchange())
                .with(ORDER_PAY_ROUTING_KEY);
    }
    
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setMandatory(true);
        
        // 确认回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("消息发送失败: {}", cause);
            }
        });
        // 返回回调
        /*template.setReturnsCallback(returned -> {
            log.error("消息路由失败: {}", returned.getReplyText());
        });*/
        
        return template;
    }
}