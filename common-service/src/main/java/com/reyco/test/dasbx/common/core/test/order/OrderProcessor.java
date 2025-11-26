package com.reyco.test.dasbx.common.core.test.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reyco.dasbx.config.exception.core.BusinessException;

@Service
public class OrderProcessor {
    
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderMetrics orderMetrics;
    
    /**
     * 处理订单核心业务逻辑
     * @throws BusinessException 
     */
    @Transactional
    public OrderResult processOrder(OrderRequest orderRequest) throws BusinessException {
        String orderId = orderRequest.getOrderId();
        long startTime = System.currentTimeMillis();
        
        log.info("开始处理订单: {}", orderId);
        
        try {
            // 1. 创建订单记录
            Order order = createOrderEntity(orderRequest);
            orderRepository.save(order);
            
            // 2. 确认扣减库存（将预占库存转为实际扣减）
            if (!inventoryService.confirmInventory(orderId)) {
                throw new BusinessException("库存确认失败");
            }
            
            // 3. 计算最终金额
            BigDecimal finalAmount = calculateFinalAmount(orderRequest);
            order.setFinalAmount(finalAmount);
            orderRepository.save(order);
            
            // 4. 构建成功结果
            OrderResult result = new OrderResult();
            result.setOrderId(orderId);
            result.setStatus(OrderStatus.CREATED.getCode());
            result.setMessage("订单创建成功");
            result.setFinalAmount(finalAmount);
            result.setCreateTime(order.getCreateTime());
            result.setCompleteTime(new Date());
            
            // 记录指标
            long duration = System.currentTimeMillis() - startTime;
            orderMetrics.recordOrderProcess(duration, true);
            
            log.info("订单处理成功: {}", orderId);
            return result;
            
        } catch (Exception e) {
            log.error("订单处理失败: {}", orderId, e);
            
            // 释放预占库存
            inventoryService.releaseInventory(orderId);
            
            // 记录指标
            long duration = System.currentTimeMillis() - startTime;
            orderMetrics.recordOrderProcess(duration, false);
            
            throw new BusinessException("订单处理失败: " + e.getMessage());
        }
    }
    
    private Order createOrderEntity(OrderRequest request) {
        Order order = new Order();
        order.setOrderId(request.getOrderId());
        order.setUserId(request.getUserId());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(OrderStatus.CREATED.getCode());
        order.setShippingAddress(request.getShippingAddress());
        order.setRemark(request.getRemark());
        
        // 设置订单项
        List<OrderItem> orderItems = request.getItems().stream()
            .map(item -> createOrderItem(item, order))
            .collect(Collectors.toList());
        order.setItems(orderItems);
        return order;
    }
    
    private OrderItem createOrderItem(OrderItemRequest itemRequest, Order order){
        // 查询商品信息
        Product product = productRepository.findById(itemRequest.getProductId());
        if(product==null) {
        	try {
				throw new BusinessException("商品不存在: " + itemRequest.getProductId());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getImageUrl());
        orderItem.setSkuCode(itemRequest.getSkuCode());
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setPrice(product.getPrice());
        orderItem.setActualPrice(product.getPrice());
        
        return orderItem;
    }
    
    private BigDecimal calculateFinalAmount(OrderRequest request) {
        // 简化实现，实际项目中需要计算优惠券、折扣、运费等
        return request.getTotalAmount();
    }
}