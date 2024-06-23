package com.reyco.dasbx.user.core.model.dto.sys;

import java.util.List;

import com.reyco.dasbx.model.dto.SimpleInsertDto;

public class SysRoleInsertDto extends SimpleInsertDto{
	private String name;
	private List<Long> menuIdList;
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
}
