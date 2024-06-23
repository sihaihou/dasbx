package com.reyco.dasbx.portal.model.domain.po;

import com.reyco.dasbx.model.po.SimpleSelectPO;

public class VideoSelectPO extends SimpleSelectPO{
	private Long categoryId;
	private String name;
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
