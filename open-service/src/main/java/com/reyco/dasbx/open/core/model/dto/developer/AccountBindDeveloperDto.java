package com.reyco.dasbx.open.core.model.dto.developer;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class AccountBindDeveloperDto extends SimpleUpdateDto{
	private String uid;
	private Long developerId;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Long getDeveloperId() {
		return developerId;
	}
	public void setDeveloperId(Long developerId) {
		this.developerId = developerId;
	}
}
