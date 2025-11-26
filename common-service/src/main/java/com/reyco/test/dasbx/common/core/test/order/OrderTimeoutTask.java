package com.reyco.test.dasbx.common.core.test.order;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderTimeoutTask {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * 处理超时未支付订单
     * 每5分钟执行一次
     */
    @Scheduled(fixedRate = 300000) // 5分钟
    public void processTimeoutOrders() {
        log.info("开始处理超时未支付订单");
        
        try {
            // 查找30分钟前创建的未支付订单
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -30);
            Date expireTime = calendar.getTime();
            
            List<Order> expiredOrders = orderRepository.findExpiredUnpaidOrders(expireTime);
            
            for (Order order : expiredOrders) {
                try {
                    // 取消订单
                    order.setStatus(OrderStatus.PAYMENT_TIMEOUT.getCode());
                    orderRepository.save(order);
                    
                    // 释放库存（如果是预占模式）
                    inventoryService.releaseInventory(order.getOrderId());
                    
                    log.info("订单超时取消成功: {}", order.getOrderId());
                    
                } catch (Exception e) {
                    log.error("处理超时订单失败: {}", order.getOrderId(), e);
                }
            }
            
            log.info("超时订单处理完成，共处理 {} 个订单", expiredOrders.size());
            
        } catch (Exception e) {
            log.error("处理超时订单任务异常", e);
        }
    }
}