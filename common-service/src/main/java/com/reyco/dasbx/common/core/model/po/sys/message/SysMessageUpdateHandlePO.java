package com.reyco.dasbx.common.core.model.po.sys.message;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class SysMessageUpdateHandlePO extends SimpleUpdatePO{
	private Byte handle;
	private Long gmtHandle;
	private Long handleBy;
	public Byte getHandle() {
		return handle;
	}
	public void setHandle(Byte handle) {
		this.handle = handle;
	}
	public Long getGmtHandle() {
		return gmtHandle;
	}
	public void setGmtHandle(Long gmtHandle) {
		this.gmtHandle = gmtHandle;
	}
	public Long getHandleBy() {
		return handleBy;
	}
	public void setHandleBy(Long handleBy) {
		this.handleBy = handleBy;
	}
}
