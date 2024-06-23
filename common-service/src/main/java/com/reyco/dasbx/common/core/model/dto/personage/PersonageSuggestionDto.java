package com.reyco.dasbx.common.core.model.dto.personage;

import com.reyco.dasbx.model.dto.BaseDto;

public class PersonageSuggestionDto implements BaseDto {
	private String keyword;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
