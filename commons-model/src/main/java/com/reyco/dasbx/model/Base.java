package com.reyco.dasbx.model;

import com.reyco.dasbx.commons.utils.serializable.ToString;

/**
 * 基础实体类
 * @author reyco
 *
 */
public class Base extends ToString {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5017380844860728654L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Long gmtCreate;
	/** 
	 * 创建人
	 */
	private Long createBy;
	/** 
	 * 修改时间
	 */
	private Long gmtModified;
	/**
	 *  修改人
	 */
	private Long modifiedBy;
	/**
	 * 删除状态
	 */
	private Byte deleted;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
	}
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Byte getDeleted() {
		return deleted;
	}
	public void setDeleted(Byte deleted) {
		this.deleted = deleted;
	}
	
}
