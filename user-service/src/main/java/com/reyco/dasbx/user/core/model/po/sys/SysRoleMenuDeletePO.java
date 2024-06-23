package com.reyco.dasbx.user.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleDeletePO;

public class SysRoleMenuDeletePO extends SimpleDeletePO {
	private Long roleId;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
