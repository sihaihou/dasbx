package com.reyco.test.dasbx.common.core.test.order;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {
    
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private OrderService orderService;
    
    /**
     * 创建订单（异步方式）
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderResult>> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
        log.info("收到订单请求, userId: {}", orderRequest.getUserId());
        
        try {
            OrderResult result = orderService.submitOrder(orderRequest);
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (BusinessException e) {
            log.warn("订单创建业务异常: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
            
        } catch (Exception e) {
            log.error("订单创建系统异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("系统繁忙，请稍后重试"));
        }
    }
    
    /**
     * 同步创建订单
     */
    @PostMapping("/create-sync")
    public ResponseEntity<ApiResponse<OrderResult>> createOrderSync(@RequestBody @Valid OrderRequest orderRequest) {
        log.info("收到同步订单请求, userId: {}", orderRequest.getUserId());
        
        try {
            OrderResult result = orderService.createOrderSync(orderRequest);
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (BusinessException e) {
            log.warn("同步订单创建业务异常: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
            
        } catch (Exception e) {
            log.error("同步订单创建系统异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("系统繁忙，请稍后重试"));
        }
    }
    
    /**
     * 查询订单状态
     */
    @GetMapping("/status/{orderId}")
    public ResponseEntity<ApiResponse<OrderResult>> getOrderStatus(@PathVariable String orderId) {
        try {
            OrderResult result = orderService.getOrderStatus(orderId);
            return ResponseEntity.ok(ApiResponse.success(result));
            
        } catch (Exception e) {
            log.error("查询订单状态异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("查询失败"));
        }
    }
    
    /**
     * 获取用户订单列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            // 这里可以实现分页查询用户订单
            // List<Order> orders = orderService.getUserOrders(userId, page, size);
            // return ResponseEntity.ok(ApiResponse.success(orders));
            
            return ResponseEntity.ok(ApiResponse.success(Collections.emptyList()));
            
        } catch (Exception e) {
            log.error("查询用户订单列表异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("查询失败"));
        }
    }
}