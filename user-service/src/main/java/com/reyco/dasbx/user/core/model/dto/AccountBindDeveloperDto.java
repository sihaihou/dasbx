package com.reyco.dasbx.user.core.model.dto;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;
/**
 * 绑定开发者信息
 * @author reyco
 *
 */
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
