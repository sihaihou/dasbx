package com.reyco.dasbx.common.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class SysEsErrorProcessPO extends SimpleUpdatePO {
	private Byte processed;

	public Byte getProcessed() {
		return processed;
	}
	public void setProcessed(Byte processed) {
		this.processed = processed;
	}
}
