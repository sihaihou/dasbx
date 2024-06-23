package com.reyco.dasbx.common.core.model.dto.sys.message;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class SysMessageUpdateHandleDto extends SimpleUpdateDto{
	private Byte handle;

	public Byte getHandle() {
		return handle;
	}

	public void setHandle(Byte handle) {
		this.handle = handle;
	}
}
