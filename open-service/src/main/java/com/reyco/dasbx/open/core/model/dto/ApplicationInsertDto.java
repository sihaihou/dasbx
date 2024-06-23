package com.reyco.dasbx.open.core.model.dto;

import com.reyco.dasbx.model.dto.SimpleInsertDto;

public class ApplicationInsertDto extends SimpleInsertDto {
	private String name;
	private String firstCategoryId;
	private String secondCategoryId;
	private String introduction;
	private String logoUri;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstCategoryId() {
		return firstCategoryId;
	}
	public void setFirstCategoryId(String firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}
	public String getSecondCategoryId() {
		return secondCategoryId;
	}
	public void setSecondCategoryId(String secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getLogoUri() {
		return logoUri;
	}
	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}
}
