package com.reyco.test.dasbx.common.core.test.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyco.dasbx.config.exception.core.BusinessException;

@Service
public class OrderService {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private InventoryTransactionRepository transactionService;
    
    private Integer orderStatusTtl = 30;
    
    private static final String ORDER_STATUS_KEY = "order:status:";
    
    /**
     * 提交订单到消息队列
     * @throws BusinessException 
     */
    public OrderResult submitOrder(OrderRequest orderRequest) throws BusinessException {
        // 参数校验
        if (orderRequest.getUserId() == null || orderRequest.getItems() == null || 
            orderRequest.getItems().isEmpty()) {
			throw new BusinessException("订单参数不完整");
        }
        
        // 生成订单ID
        String orderId = generateOrderId();
        orderRequest.setOrderId(orderId);
        orderRequest.setTimestamp(System.currentTimeMillis());
        
        // 预存订单状态到Redis
        OrderResult initialResult = new OrderResult();
        initialResult.setOrderId(orderId);
        initialResult.setStatus(OrderStatus.PROCESSING.getCode());
        initialResult.setMessage("订单正在处理中");
        initialResult.setCreateTime(new Date());
        
        redisTemplate.opsForValue().set(ORDER_STATUS_KEY + orderId, initialResult, 
            Duration.ofMinutes(orderStatusTtl));
        
        try {
            // 1. 预占库存
            if (!inventoryService.reserveInventory(orderRequest.getItems(), orderId)) {
                throw new BusinessException("库存不足");
            }
            
            // 2. 发送消息到RabbitMQ
            CorrelationData correlationData = new CorrelationData(orderId);
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                orderRequest,
                message -> {
                    message.getMessageProperties().setMessageId(orderId);
                    message.getMessageProperties().setTimestamp(new Date());
                    return message;
                },
                correlationData
            );
            
            //在补偿业务中，事务状态(InventoryTransaction)处于预占库存扣减完成，但订单信息未发送到MQ时，需要重新发送MQ或者回滚库存扣减逻辑
            InventoryTransaction transaction = new InventoryTransaction();
            transaction.setOrderId(orderId);
            transaction.setStatus(InventoryTransactionStatus.RESERVED);
            transaction.setNewStatus(InventoryTransactionStatus.SENDMQED);
            transactionService.updateStatus(transaction);
            
            log.info("订单已进入消息队列, orderId: {}, userId: {}", orderId, orderRequest.getUserId());
            return initialResult;
            
        } catch (BusinessException e) {
            // 业务异常，释放预占库存
            inventoryService.releaseInventory(orderId);
            
            // 更新状态为失败
            initialResult.setStatus(OrderStatus.FAILED.getCode());
            initialResult.setMessage(e.getMessage());
            redisTemplate.opsForValue().set(ORDER_STATUS_KEY + orderId, initialResult);
            
            throw e;
        } catch (Exception e) {
            log.error("订单提交到消息队列失败", e);
            
            // 释放预占库存
            inventoryService.releaseInventory(orderId);
            
            // 更新状态为失败
            initialResult.setStatus(OrderStatus.FAILED.getCode());
            initialResult.setMessage("系统繁忙，请稍后重试");
            redisTemplate.opsForValue().set(ORDER_STATUS_KEY + orderId, initialResult);
            
            throw new BusinessException("系统繁忙，请稍后重试");
        }
    }
    
    /**
     * 查询订单处理状态
     */
    public OrderResult getOrderStatus(String orderId) {
        OrderResult result = (OrderResult) redisTemplate.opsForValue()
            .get(ORDER_STATUS_KEY + orderId);
        
        if (result == null) {
            result = new OrderResult();
            result.setOrderId(orderId);
            result.setStatus(OrderStatus.NOT_FOUND.getCode());
            result.setMessage("订单不存在或已过期");
        }
        
        return result;
    }
    
    /**
     * 直接创建订单（同步方式，用于小流量或管理后台）
     * @throws BusinessException 
     */
    @Transactional
    public OrderResult createOrderSync(OrderRequest orderRequest) throws BusinessException {
        // 参数校验
        validateOrderRequest(orderRequest);
        
        // 生成订单ID
        String orderId = generateOrderId();
        
        try {
            // 1. 检查库存
            if (!inventoryService.checkInventory(orderRequest.getItems())) {
                throw new BusinessException("库存不足");
            }
            
            // 2. 扣减库存
            if (!inventoryService.deductInventory(orderRequest.getItems())) {
                throw new BusinessException("库存扣减失败");
            }
            
            // 3. 创建订单
            Order order = createOrderEntity(orderRequest, orderId);
            
            // 4. 计算最终金额
            BigDecimal finalAmount = calculateFinalAmount(orderRequest);
            order.setFinalAmount(finalAmount);
            
            // 5. 返回结果
            OrderResult result = new OrderResult();
            result.setOrderId(orderId);
            result.setStatus(OrderStatus.CREATED.getCode());
            result.setMessage("订单创建成功");
            result.setFinalAmount(finalAmount);
            result.setCreateTime(new Date());
            
            return result;
            
        } catch (Exception e) {
            // 回滚库存
            inventoryService.rollbackInventory(orderRequest.getItems());
            throw new BusinessException("订单创建失败: " + e.getMessage());
        }
    }
    
    private void validateOrderRequest(OrderRequest orderRequest) throws BusinessException {
        if (orderRequest.getUserId() == null) {
            throw new BusinessException("用户ID不能为空");
        }
        
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new BusinessException("订单商品不能为空");
        }
        
        for (OrderItemRequest item : orderRequest.getItems()) {
            if (item.getProductId() == null || item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BusinessException("商品信息不完整");
            }
        }
    }
    
    private Order createOrderEntity(OrderRequest request, String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(request.getUserId());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(OrderStatus.CREATED.getCode());
        order.setShippingAddress(request.getShippingAddress());
        order.setRemark(request.getRemark());
        
        // 设置订单项
        List<OrderItem> orderItems = request.getItems().stream()
            .map(item -> {
				try {
					return createOrderItem(item, order);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			})
            .collect(Collectors.toList());
        
        order.setItems(orderItems);
        return order;
    }
    
    private OrderItem createOrderItem(OrderItemRequest itemRequest, Order order) throws BusinessException {
        // 查询商品信息
        Product product = productRepository.findById(itemRequest.getProductId());
        if(product==null) {
        	throw new BusinessException("商品不存在: " + itemRequest.getProductId());
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getImageUrl());
        orderItem.setSkuCode(itemRequest.getSkuCode());
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setPrice(product.getPrice());
        orderItem.setActualPrice(product.getPrice()); // 实际价格，可能包含折扣
        
        return orderItem;
    }
    
    private BigDecimal calculateFinalAmount(OrderRequest request) {
        // 这里可以实现优惠券、折扣、运费等计算逻辑
        // 简化实现，直接返回总金额
        return request.getTotalAmount();
    }
    
    private String generateOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        String randomStr = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        return "ORD" + timeStr + randomStr;
    }
}