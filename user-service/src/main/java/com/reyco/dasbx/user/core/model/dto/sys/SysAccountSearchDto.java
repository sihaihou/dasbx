package com.reyco.dasbx.user.core.model.dto.sys;

import com.reyco.dasbx.es.core.search.SimpleElasticsearchDto;

public class SysAccountSearchDto extends SimpleElasticsearchDto{
	private Byte gender;
	private Byte state;
	private Byte type;
	public Byte getGender() {
		return gender;
	}
	public void setGender(Byte gender) {
		this.gender = gender;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
}
