package com.reyco.dasbx.user.core.model.vo.sys;

import java.util.List;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.vo.InfoVO;

public class SysRoleInfoVO implements InfoVO{
    private Long id;
    private String name;
    
    private List<Long> menuIdList;
    
    private Long createBy;
    private String createByDesc;
    
    private Long gmtCreate;
    private String gmtCreateDesc;
    
    private Long modifiedBy;
    private String modifiedByDesc;
    
    private Long gmtModified;
    private String gmtModifiedDesc;
    
    private String remark;
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
	public List<Long> getMenuIdList() {
		return menuIdList;
	}
	public void setMenuIdList(List<Long> menuIdList) {
		this.menuIdList = menuIdList;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public String getCreateByDesc() {
		return createByDesc;
	}
	public void setCreateByDesc(String createByDesc) {
		this.createByDesc = createByDesc;
	}
	public Long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
		if(gmtCreate!=null) {
			this.gmtCreateDesc = Dasbx.getDateByTimeZone(gmtCreate);
		}
	}
	public String getGmtCreateDesc() {
		return gmtCreateDesc;
	}
	public void setGmtCreateDesc(String gmtCreateDesc) {
		this.gmtCreateDesc = gmtCreateDesc;
	}
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedByDesc() {
		return modifiedByDesc;
	}
	public void setModifiedByDesc(String modifiedByDesc) {
		this.modifiedByDesc = modifiedByDesc;
	}
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
		if(gmtModified!=null) {
			this.gmtModifiedDesc = Dasbx.getDateByTimeZone(gmtModified);
		}
	}
	public String getGmtModifiedDesc() {
		return gmtModifiedDesc;
	}
	public void setGmtModifiedDesc(String gmtModifiedDesc) {
		this.gmtModifiedDesc = gmtModifiedDesc;
	}
}
