package com.reyco.dasbx.user.core.model.domain;

import com.reyco.dasbx.commons.utils.ToString;

/**
 * @author reyco
 * @version v1.0.1
 * @date 2021.09.16
 */
public class SysRole extends ToString {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5987078177366904931L;
	/**
     * id
     */
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Long gmtCreate;
    /**
     * 修改者
     */
    private Long modifiedBy;
    /**
     * 修改时间
     */
    private Long gmtModified;
    /**
     * 数据状态  0正常  1删除
     */
    private Integer deleted;
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
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public Long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
	}
	public Integer getDeleted() {
		return deleted;
	}
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
}
