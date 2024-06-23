package com.reyco.dasbx.user.core.model.po.sys;

import java.util.List;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class SysRoleMenuInsertPO extends SimpleInsertPO{
	private Long roleId;
	private List<Long> menuIdList;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public List<Long> getMenuIdList() {
		return menuIdList;
	}
	public void setMenuIdList(List<Long> menuIdList) {
		this.menuIdList = menuIdList;
	}
}
