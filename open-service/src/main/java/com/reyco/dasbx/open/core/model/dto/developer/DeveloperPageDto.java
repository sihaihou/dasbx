package com.reyco.dasbx.open.core.model.dto.developer;

import com.reyco.dasbx.model.dto.SimplePageDto;

public class DeveloperPageDto extends SimplePageDto{
	private String keyword;
	private Byte type;
	private Byte state;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
}
