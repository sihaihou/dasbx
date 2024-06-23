package com.reyco.dasbx.model.po;

public class SimpleDeletePO extends SimpleUpdatePO implements DeletePO {
	private Byte deleted;
	@Override
	public Byte getDeleted() {
		return deleted;
	}
	public void setDeleted(Byte deleted) {
		this.deleted = deleted;
	}
}
