package com.reyco.dasbx.user.core.model.po;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class AccountBindDeveloperPO extends SimpleUpdatePO{
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
