package com.reyco.dasbx.user.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleDeletePO;

public class SysUserRoleDeletePO extends SimpleDeletePO {
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
