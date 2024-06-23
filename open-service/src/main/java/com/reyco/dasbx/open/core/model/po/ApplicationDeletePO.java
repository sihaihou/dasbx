package com.reyco.dasbx.open.core.model.po;

import com.reyco.dasbx.model.po.SimpleDeletePO;

public class ApplicationDeletePO extends SimpleDeletePO {
	private Long id;
	private Byte deleted;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getDeleted() {
		return deleted;
	}
	public void setDeleted(Byte deleted) {
		this.deleted = deleted;
	}
}
