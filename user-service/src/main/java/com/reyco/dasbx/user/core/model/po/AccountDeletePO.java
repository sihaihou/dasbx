package com.reyco.dasbx.user.core.model.po;

import com.reyco.dasbx.model.po.SimpleDeletePO;

public class AccountDeletePO extends SimpleDeletePO {
	private Byte deleted;
	private Long gmtModified;
	private Long modifiedBy;
	@Override
	public Byte getDeleted() {
		return deleted;
	}
	public void setDeleted(Byte deleted) {
		this.deleted = deleted;
	}
	@Override
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
	}
	@Override
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
