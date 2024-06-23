package com.reyco.dasbx.user.core.model.dto.sys;

import com.reyco.dasbx.model.dto.SimplePageDto;

public class SysAccountPageDto extends SimplePageDto {
	private String keyword;
	private Byte gender;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Byte getGender() {
		return gender;
	}
	public void setGender(Byte gender) {
		this.gender = gender;
	}
}
