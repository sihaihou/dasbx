package com.reyco.dasbx.open.core.model.po;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class ApplicationUpdateSimplePO extends SimpleUpdatePO {
	private String firstCategoryId;
	private String secondCategoryId;
	private String name;
	private String introduction;
	private String accounts;
	private String logoUri;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public String getLogoUri() {
		return logoUri;
	}
	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}
}
