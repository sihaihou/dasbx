package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.model.dto.SimplePageDto;

public class VideoPageDto extends SimplePageDto{
	private Long categoryId;
	private String keyword;
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
