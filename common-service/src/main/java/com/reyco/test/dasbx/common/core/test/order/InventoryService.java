package com.reyco.test.dasbx.common.core.test.order;

import java.util.List;

public interface InventoryService {
    
    boolean checkInventory(List<OrderItemRequest> items);
    
    boolean reserveInventory(List<OrderItemRequest> items, String orderId);
    
    boolean confirmInventory(String orderId);
    
    boolean releaseInventory(String orderId);
    
    boolean deductInventory(List<OrderItemRequest> items);
    
    boolean rollbackInventory(List<OrderItemRequest> items);
    
}