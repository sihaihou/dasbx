package com.reyco.dasbx.user.core.model.dto.sys;

import java.util.List;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class SysRoleUpdateDto extends SimpleUpdateDto{
	private String name;
	private List<Long> menuIdList;
	private String remark;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Long> getMenuIdList() {
		return menuIdList;
	}
	public void setMenuIdList(List<Long> menuIdList) {
		this.menuIdList = menuIdList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
