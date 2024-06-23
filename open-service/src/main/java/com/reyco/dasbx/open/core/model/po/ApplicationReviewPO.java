package com.reyco.dasbx.open.core.model.po;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class ApplicationReviewPO extends SimpleUpdatePO{
	private Byte status;

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
}
