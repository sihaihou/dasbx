package com.reyco.dasbx.open.core.model.po;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class ApplicationInsertPO extends SimpleInsertPO{
	private String name;
	private String firstCategoryId;
	private String secondCategoryId;
	private String clientId;
	private String clientSecret;
	private String introduction;
	private String logoUri;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstCategoryId() {
		return firstCategoryId;
	}
	public void setFirstCategoryId(String firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}
	public String getSecondCategoryId() {
		return secondCategoryId;
	}
	public void setSecondCategoryId(String secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
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
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getLogoUri() {
		return logoUri;
	}
	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}
}
