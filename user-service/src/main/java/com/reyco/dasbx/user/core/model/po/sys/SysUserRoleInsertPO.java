package com.reyco.dasbx.user.core.model.po.sys;

import java.util.List;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class SysUserRoleInsertPO extends SimpleInsertPO{
	private Long userId;
	private List<Long> roleIdList;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<Long> getRoleIdList() {
		return roleIdList;
	}
	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}
	
}
