package com.reyco.test.dasbx.common.core.test.order;

import java.util.Date;
import java.util.List;

public class InventoryTransaction {
	private String id;
	private String orderId;
	private InventoryTransactionStatus status;
	private InventoryTransactionStatus newStatus;
	private Date createTime;
	private List<InventoryTransactionItem> Items;
	private Integer retryCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public InventoryTransactionStatus getStatus() {
		return status;
	}
	public void setStatus(InventoryTransactionStatus status) {
		this.status = status;
	}
	public InventoryTransactionStatus getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(InventoryTransactionStatus newStatus) {
		this.newStatus = newStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<InventoryTransactionItem> getItems() {
		return Items;
	}
	public void setItems(List<InventoryTransactionItem> items) {
		Items = items;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
}
