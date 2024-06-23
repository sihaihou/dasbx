package com.reyco.dasbx.common.core.model.dto.sys.message;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class SysMessageUpdateReadDto extends SimpleUpdateDto{
	private Byte read;
	public Byte getRead() {
		return read;
	}
	public void setRead(Byte read) {
		this.read = read;
	}
}
