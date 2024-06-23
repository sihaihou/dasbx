package com.reyco.dasbx.model.domain;

import com.reyco.dasbx.model.Base;

public class SysEsError extends Base{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2469940505415816141L;
	private String index;
	private String type;
	private String primaryKey;
	private String failureMessage;
	private Byte processed;
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getFailureMessage() {
		return failureMessage;
	}
	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
	public Byte getProcessed() {
		return processed;
	}
	public void setProcessed(Byte processed) {
		this.processed = processed;
	}
}
