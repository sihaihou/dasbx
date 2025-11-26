package com.reyco.test.dasbx.common.core.test.order;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageConsumer {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private OrderProcessor orderProcessor;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private OrderMetrics orderMetrics;
    
    private static final String ORDER_STATUS_KEY = "order:status:";
    
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void processOrder(OrderRequest orderRequest, Message message) {
        String orderId = orderRequest.getOrderId();
        log.info("开始处理订单消息: {}", orderId);
        
        try {
            // 处理订单
            OrderResult result = orderProcessor.processOrder(orderRequest);
            
            // 更新Redis中的订单状态
            redisTemplate.opsForValue().set(ORDER_STATUS_KEY + orderId, result);
            
            log.info("订单消息处理完成: {}, 状态: {}", orderId, result.getStatus());
            
            // 记录成功指标
            orderMetrics.recordOrderMessageProcess(true);
            
        } catch (Exception e) {
            log.error("订单消息处理失败: {}", orderId, e);
            
            // 更新为失败状态
            OrderResult failedResult = new OrderResult();
            failedResult.setOrderId(orderId);
            failedResult.setStatus(OrderStatus.FAILED.getCode());
            failedResult.setMessage("订单处理失败: " + e.getMessage());
            failedResult.setCompleteTime(new Date());
            
            redisTemplate.opsForValue().set(ORDER_STATUS_KEY + orderId, failedResult);
            
            // 记录失败指标
            orderMetrics.recordOrderMessageProcess(false);
            
            // 可以发送到死信队列进行进一步处理
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
    
    /**
     * 死信队列处理
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_DLQ)
    public void processFailedOrder(OrderRequest orderRequest, Message message) {
        String orderId = orderRequest.getOrderId();
        log.warn("处理死信队列中的订单: {}", orderId);
        
        // 记录失败日志
        log.error("订单进入死信队列, orderId: {}, 原始消息: {}", orderId, message.toString());
        
        // 可以在这里实现补偿逻辑，比如发送告警、人工处理等
        // sendAlert(orderId, "订单处理失败进入死信队列");
    }
}