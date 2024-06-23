package com.reyco.dasbx.user.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class SysAccountDisableOrEnablePO extends SimpleUpdatePO{
	private Byte state;
	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}
}
