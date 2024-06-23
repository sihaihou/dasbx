package com.reyco.dasbx.model.po;

public class SimpleInsertPO implements InsertPO {
	private Long id;
	protected String remark;
	protected Byte deleted;
	protected Long gmtCreate;
	protected Long createBy;
	protected Long gmtModified;
	protected Long modifiedBy;
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public Byte getDeleted() {
		return deleted;
	}
	public void setDeleted(Byte deleted) {
		this.deleted = deleted;
	}
	public Long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
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
