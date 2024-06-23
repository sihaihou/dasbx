package com.reyco.dasbx.open.core.model.vo.applicationCategory;

import com.reyco.dasbx.model.vo.InfoVO;

public class ApplicationCategoryInfoVo implements InfoVO{
	private Long id;
	private String name;
    private Long parentId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
