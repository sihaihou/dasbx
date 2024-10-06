package com.reyco.dasbx.model.msg;

import com.reyco.dasbx.commons.utils.serializable.ToString;
import com.reyco.dasbx.model.constants.OperationType;

public class SysAccountSyncEsMessage extends ToString implements RabbitMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 546159949829627021L;
	private Long accountId;
	private OperationType type;
	@Override
	public String getCorrelationDataId() {
		return accountId.toString();
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public OperationType getType() {
		return type;
	}
	public void setType(OperationType type) {
		this.type = type;
	}
}
