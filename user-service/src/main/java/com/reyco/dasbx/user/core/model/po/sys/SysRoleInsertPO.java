package com.reyco.dasbx.user.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class SysRoleInsertPO extends SimpleInsertPO {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
