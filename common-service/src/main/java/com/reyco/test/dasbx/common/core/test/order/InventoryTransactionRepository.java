package com.reyco.test.dasbx.common.core.test.order;

import java.util.Date;
import java.util.List;

public interface InventoryTransactionRepository {
	
	void save(InventoryTransaction transaction);
	
	List<InventoryTransaction> findTimeoutTransactions(Date timeoutTime,InventoryTransactionStatus inventoryTransactionStatus);
	
	void updateStatus(InventoryTransaction transaction);
	
}
