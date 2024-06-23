package com.reyco.dasbx.common.core.model.dto.sys.message;

import com.reyco.dasbx.model.dto.SimplePageDto;


public class SysMessageSelectDto extends SimplePageDto{
	private Long userId;
	private Byte read;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Byte getRead() {
		return read;
	}
	public void setRead(Byte read) {
		this.read = read;
	}
}
