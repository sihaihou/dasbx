package com.reyco.dasbx.user.core.model.es.po;

import java.util.List;

import com.reyco.dasbx.es.core.client.ElasticsearchDocument;

public class SysRoleElasticsearchDocument implements ElasticsearchDocument{
	private Long id;
	private String name;
	private String remark;
	private String gmtCreate;
	private String createBy;
	private String gmtModified;
	private String modifiedBy;
	private List<String> suggestion;
	@Override
	public String getPrimaryKeyId() {
		return id.toString();
	}
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
	public String getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(String gmtModified) {
		this.gmtModified = gmtModified;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public List<String> getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(List<String> suggestion) {
		this.suggestion = suggestion;
	}
}
