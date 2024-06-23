package com.reyco.dasbx.open.core.model.dto;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class ApplicationUpdateDto extends SimpleUpdateDto {
	private String name;
	private String redirectUri;
	private String desc;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
