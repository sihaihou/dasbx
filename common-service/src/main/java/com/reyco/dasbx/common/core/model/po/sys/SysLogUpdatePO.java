package com.reyco.dasbx.common.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class SysLogUpdatePO extends SimpleUpdatePO {
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
