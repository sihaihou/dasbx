package com.reyco.dasbx.user.core.model.vo.sys;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.vo.ListVO;

public class SysRoleListVO implements ListVO{
	private Long id;
	private String name;
	private String remark;
	private Long gmtCreate;
	private String gmtCreateDesc;
	private Long gmtModified;
	private String gmtModifiedDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
		this.gmtCreateDesc = Dasbx.getDateByTimeZone(gmtCreate);
	}
	public String getGmtCreateDesc() {
		return gmtCreateDesc;
	}
	public void setGmtCreateDesc(String gmtCreateDesc) {
		this.gmtCreateDesc = gmtCreateDesc;
	}
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
		this.gmtModifiedDesc = Dasbx.getDateByTimeZone(gmtModified);
	}
	public String getGmtModifiedDesc() {
		return gmtModifiedDesc;
	}
	public void setGmtModifiedDesc(String gmtModifiedDesc) {
		this.gmtModifiedDesc = gmtModifiedDesc;
	}
}
