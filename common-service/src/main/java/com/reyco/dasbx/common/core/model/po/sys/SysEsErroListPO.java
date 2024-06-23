package com.reyco.dasbx.common.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleSelectPO;

public class SysEsErroListPO extends SimpleSelectPO{
	private String index;
	private String type;
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
	public Byte getProcessed() {
		return processed;
	}
	public void setProcessed(Byte processed) {
		this.processed = processed;
	}
}
