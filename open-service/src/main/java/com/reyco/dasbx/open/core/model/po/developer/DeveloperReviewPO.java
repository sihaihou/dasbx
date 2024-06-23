package com.reyco.dasbx.open.core.model.po.developer;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class DeveloperReviewPO extends SimpleUpdatePO {
	private Byte state;

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}
}
