package com.reyco.dasbx.model.dto;

public class SimpleInsertDto implements InsertDto {
	private String remark;
	private Byte deleted;
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Byte getDeleted() {
		return deleted;
	}
	public void setDeleted(Byte deleted) {
		this.deleted = deleted;
	}
}
