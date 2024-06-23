package com.reyco.dasbx.common.core.model.po.sys.message;

import com.reyco.dasbx.model.po.SimpleSelectPO;

public class SysMessageSelectPO extends SimpleSelectPO {
	
	private Long userId;
	
	private Byte read;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Byte getRead() {
		return read;
	}
	public void setRead(Byte read) {
		this.read = read;
	}
}
