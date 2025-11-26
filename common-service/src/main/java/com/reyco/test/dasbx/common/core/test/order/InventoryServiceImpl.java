package com.reyco.test.dasbx.common.core.test.order;

import java.time.Duration;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private InventoryTransactionRepository transactionRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    private static final String PRODUCT_STOCK_KEY = "product:stock:";
    private static final String STOCK_RESERVATION_KEY = "stock:reservation:";
    
    @Override
    public boolean checkInventory(List<OrderItemRequest> items) {
        for (OrderItemRequest item : items) {
            Integer availableStock = getAvailableStock(item.getProductId());
            if (availableStock == null || availableStock < item.getQuantity()) {
                log.warn("商品库存不足, productId: {}, 需要: {}, 可用: {}", 
                        item.getProductId(), item.getQuantity(), availableStock);
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean reserveInventory(List<OrderItemRequest> items, String orderId) {
        // 在数据库中创建预占事务记录
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setOrderId(orderId);
        transaction.setStatus(InventoryTransactionStatus.PROCESSING);
        transaction.setCreateTime(new Date());
        
        List<InventoryTransactionItem> transactionItems = items.stream()
            .map(item -> {
                InventoryTransactionItem transactionItem = new InventoryTransactionItem();
                transactionItem.setProductId(item.getProductId());
                transactionItem.setQuantity(item.getQuantity());
                return transactionItem;
            })
            .collect(Collectors.toList());
        
        transaction.setItems(transactionItems);
        transactionRepository.save(transaction);
        try {
            // 执行库存预占
            for (OrderItemRequest item : items) {
                int rows = productRepository.reserveStock(item.getProductId(), item.getQuantity());
                if (rows == 0) {
                    throw new BusinessException("库存预占失败，商品ID: " + item.getProductId());
                }
            }
            
            // 更新事务状态为预占成功
            transaction.setStatus(InventoryTransactionStatus.PROCESSING);
            transaction.setNewStatus(InventoryTransactionStatus.RESERVED);
            transactionRepository.updateStatus(transaction);
            
            // 异步更新Redis缓存
            asyncUpdateRedisCache(items);
            
            return true;
            
        } catch (Exception e) {
            // 更新事务状态为失败
            transaction.setNewStatus(InventoryTransactionStatus.FAILED);
            transactionRepository.save(transaction);
            
            // 回滚已预占的库存
            rollbackReservedStock(items);
            
            log.error("库存预占失败, orderId: {}", orderId, e);
            return false;
        }
    }
    @Async
    public void asyncUpdateRedisCache(List<OrderItemRequest> items) {
        try {
            for (OrderItemRequest item : items) {
                String productKey = PRODUCT_STOCK_KEY + item.getProductId();
                redisTemplate.opsForValue().decrement(productKey, item.getQuantity());
            }
        } catch (Exception e) {
            log.error("异步更新Redis缓存失败", e);
        }
    }
    private void rollbackReservedStock(List<OrderItemRequest> items) {
        for (OrderItemRequest item : items) {
            try {
                productRepository.releaseReservedStock(item.getProductId(), item.getQuantity());
            } catch (Exception e) {
                log.error("回滚预占库存失败, productId: {}", item.getProductId(), e);
            }
        }
    }
    /**
     * 补偿任务：处理悬挂的事务
     */
    @Scheduled(fixedRate = 300000) // 5分钟执行一次
    public void compensateTransactions() {
        log.info("开始补偿库存预占事务...");
        
        try {
            // 查找超时未完成的事务
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -5);
            Date timeoutTime = calendar.getTime();
            
            List<InventoryTransaction> timeoutTransactions = 
                transactionRepository.findTimeoutTransactions(timeoutTime, 
                    InventoryTransactionStatus.PROCESSING);
            
            for (InventoryTransaction transaction : timeoutTransactions) {
                try {
                    // 根据业务逻辑决定是重试还是回滚
                    if (shouldRetry(transaction)) {
                        retryTransaction(transaction);
                    } else {
                        rollbackTransaction(transaction);
                    }
                } catch (Exception e) {
                    log.error("补偿事务处理失败, transactionId: {}", transaction.getId(), e);
                }
            }
            
            log.info("补偿库存预占事务完成，处理 {} 个事务", timeoutTransactions.size());
            
        } catch (Exception e) {
            log.error("补偿库存预占事务异常", e);
        }
    }
    private boolean shouldRetry(InventoryTransaction transaction) {
        // 根据重试次数、业务规则等判断是否应该重试
        return transaction.getRetryCount() < 3;
    }
    
    private void retryTransaction(InventoryTransaction transaction) {
        // 重新执行库存预占逻辑
        // 注意幂等性处理
    }
    private void rollbackTransaction(InventoryTransaction transaction) {
        // 回滚事务
        List<OrderItemRequest> items = transaction.getItems().stream()
            .map(item -> {
                OrderItemRequest request = new OrderItemRequest();
                request.setProductId(item.getProductId());
                request.setQuantity(item.getQuantity());
                return request;
            })
            .collect(Collectors.toList());
        
        rollbackReservedStock(items);
        transaction.setStatus(InventoryTransactionStatus.ROLLBACK);
        transactionRepository.save(transaction);
    }
    
    @Override
    public boolean confirmInventory(String orderId) {
        String reservationKey = STOCK_RESERVATION_KEY + orderId;
        
        try {
            // 获取预占记录
            Map<String, Integer> reservationData = (Map<String, Integer>) 
                redisTemplate.opsForValue().get(reservationKey);
            
            if (reservationData == null || reservationData.isEmpty()) {
                log.warn("预占记录不存在, orderId: {}", orderId);
                return false;
            }
            
            // 异步更新数据库库存
            List<OrderItemRequest> items = reservationData.entrySet().stream()
                .map(entry -> {
                    OrderItemRequest item = new OrderItemRequest();
                    item.setProductId(Long.parseLong(entry.getKey()));
                    item.setQuantity(entry.getValue());
                    return item;
                }).collect(Collectors.toList());
            
            asyncUpdateDatabaseStock(items);
            
            // 删除预占记录
            redisTemplate.delete(reservationKey);
            
            log.info("库存确认扣减成功, orderId: {}", orderId);
            return true;
            
        } catch (Exception e) {
            log.error("确认扣减库存异常", e);
            return false;
        }
    }
    
    @Override
    public boolean releaseInventory(String orderId) {
        String reservationKey = STOCK_RESERVATION_KEY + orderId;
        
        try {
            // 获取预占记录
            Map<String, Integer> reservationData = (Map<String, Integer>) 
                redisTemplate.opsForValue().get(reservationKey);
            
            if (reservationData == null) {
                log.warn("预占记录不存在, orderId: {}", orderId);
                return true;
            }
            
            // 恢复库存
            for (Map.Entry<String, Integer> entry : reservationData.entrySet()) {
                Long productId = Long.parseLong(entry.getKey());
                Integer quantity = entry.getValue();
                increaseProductStock(productId, quantity);
                log.info("恢复预占库存, productId: {}, 数量: {}", productId, quantity);
            }
            
            // 删除预占记录
            redisTemplate.delete(reservationKey);
            
            log.info("取消预占库存成功, orderId: {}", orderId);
            return true;
            
        } catch (Exception e) {
            log.error("取消预占库存异常", e);
            return false;
        }
    }
    
    @Override
    public boolean deductInventory(List<OrderItemRequest> items) {
        // 直接扣减库存（不使用预占模式）
        String lockKey = "inventory_deduct_lock";
        String lockValue = UUID.randomUUID().toString();
        
        try {
            boolean locked = tryLock(lockKey, lockValue, 5000);
            if (!locked) {
                log.error("获取库存扣减锁失败");
                return false;
            }
            
            if (!checkInventory(items)) {
                return false;
            }
            
            for (OrderItemRequest item : items) {
                boolean success = deductProductStock(item.getProductId(), item.getQuantity());
                if (!success) {
                    rollbackDeductedItems(items.subList(0, items.indexOf(item)));
                    return false;
                }
            }
            
            asyncUpdateDatabaseStock(items);
            return true;
            
        } finally {
            releaseLock(lockKey, lockValue);
        }
    }
    
    @Override
    public boolean rollbackInventory(List<OrderItemRequest> items) {
        try {
            for (OrderItemRequest item : items) {
                increaseProductStock(item.getProductId(), item.getQuantity());
            }
            return true;
        } catch (Exception e) {
            log.error("库存回滚异常", e);
            return false;
        }
    }
    
    // ========== 私有方法 ==========
    
    private Integer getAvailableStock(Long productId) {
        String key = PRODUCT_STOCK_KEY + productId;
        Object stock = redisTemplate.opsForValue().get(key);
        
        if (stock == null) {
            // 从数据库加载
            Product product = productRepository.findById(productId);
            if (product != null) {
                redisTemplate.opsForValue().set(key, product.getStock(), Duration.ofHours(2));
                return product.getStock();
            }
            return 0;
        }
        
        return Integer.parseInt(stock.toString());
    }
    
    private boolean deductProductStock(Long productId, Integer quantity) {
        String key = PRODUCT_STOCK_KEY + productId;
        
        String luaScript = 
            "local current = redis.call('get', KEYS[1]) " +
            "if not current then return -1 end " +
            "local current_num = tonumber(current) " +
            "local deduct_num = tonumber(ARGV[1]) " +
            "if current_num >= deduct_num then " +
            "   redis.call('decrby', KEYS[1], deduct_num) " +
            "   return deduct_num " +
            "else " +
            "   return -2 " +
            "end";
        
        Long result = redisTemplate.execute(
            new DefaultRedisScript<>(luaScript, Long.class),
            Collections.singletonList(key),
            quantity.toString()
        );
        
        return result != null && result > 0;
    }
    
    private void increaseProductStock(Long productId, Integer quantity) {
        String key = PRODUCT_STOCK_KEY + productId;
        redisTemplate.opsForValue().increment(key, quantity);
    }
    
    private void rollbackDeductedItems(List<OrderItemRequest> deductedItems) {
        for (OrderItemRequest item : deductedItems) {
            increaseProductStock(item.getProductId(), item.getQuantity());
        }
    }
    
    private void rollbackReservedItems(List<OrderItemRequest> reservedItems) {
        for (OrderItemRequest item : reservedItems) {
            increaseProductStock(item.getProductId(), item.getQuantity());
        }
    }
    
    @Async("inventoryTaskExecutor")
    public void asyncUpdateDatabaseStock(List<OrderItemRequest> items) {
        try {
            for (OrderItemRequest item : items) {
                int rows = productRepository.deductStock(item.getProductId(), item.getQuantity());
                if (rows == 0) {
                    log.error("数据库库存扣减失败, productId: {}", item.getProductId());
                }
            }
        } catch (Exception e) {
            log.error("异步更新数据库库存异常", e);
        }
    }
    
    private boolean tryLock(String lockKey, String value, long expireMillis) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(
            lockKey, value, Duration.ofMillis(expireMillis)
        );
        return Boolean.TRUE.equals(success);
    }
    
    private void releaseLock(String lockKey, String value) {
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(lockKey);
            if (value.equals(currentValue)) {
                redisTemplate.delete(lockKey);
            }
        } catch (Exception e) {
            log.error("释放锁异常", e);
        }
    }
}