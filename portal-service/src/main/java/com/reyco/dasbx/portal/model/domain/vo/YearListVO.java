package com.reyco.dasbx.portal.model.domain.vo;

import com.reyco.dasbx.model.vo.ListVO;

public class YearListVO implements ListVO {
	private Long id;
	private String name;
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
}
