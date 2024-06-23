package com.reyco.dasbx.common.core.model.po.sys.message;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class SysMessageUpdateReadPO extends SimpleUpdatePO{
	private Byte read;
	private Long gmtRead;
	private Long readBy;
	public Byte getRead() {
		return read;
	}
	public void setRead(Byte read) {
		this.read = read;
	}
	public Long getGmtRead() {
		return gmtRead;
	}
	public void setGmtRead(Long gmtRead) {
		this.gmtRead = gmtRead;
	}
	public Long getReadBy() {
		return readBy;
	}
	public void setReadBy(Long readBy) {
		this.readBy = readBy;
	}
}
