package com.reyco.dasbx.common.core.model.dto.sys;

import com.reyco.dasbx.model.dto.SimplePageDto;

public class SysLogPageDto extends SimplePageDto {
	
	private String keyword;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
