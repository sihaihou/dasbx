package com.reyco.dasbx.user.core.model.dto.sys;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class SysAccountDisableOrEnableDto extends SimpleUpdatePO {
	private Byte state;

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}
	
}
