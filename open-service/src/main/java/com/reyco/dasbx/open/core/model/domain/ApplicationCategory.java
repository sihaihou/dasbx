package com.reyco.dasbx.open.core.model.domain;

import com.reyco.dasbx.model.Base;

public class ApplicationCategory extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4115346776630580195L;
	private String name;
    private Long parentId;
    private String level;
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}
