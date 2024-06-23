package com.reyco.dasbx.common.core.model.dto.sys;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class SysLogUpdateDto extends SimpleUpdateDto {
	private Long code;
	private Long userId;
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
