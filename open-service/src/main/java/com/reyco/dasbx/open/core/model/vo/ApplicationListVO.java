package com.reyco.dasbx.open.core.model.vo;

import java.util.Date;

import com.reyco.dasbx.model.vo.ListVO;

public class ApplicationListVO implements ListVO{
	private Long id;
	private String name;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String status;
	private String remark;
	private Date gmtCreate;
	private String createDesc;
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
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public String getCreateDesc() {
		return createDesc;
	}
	public void setCreateDesc(String createDesc) {
		this.createDesc = createDesc;
	}
}
