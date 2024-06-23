package com.reyco.dasbx.open.core.model.domain;

import com.reyco.dasbx.model.Base;

public class Application extends Base{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3214463087911326193L;
	private String firstCategoryId;
	private String secondCategoryId;
	private String name;
	private String domainUri;
	private String logoUri;
	private String introduction;
	private String organizer;
	private String filingNumber;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	private String accounts;
	private Byte status;
	private String createDesc;
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
	public String getDomainUri() {
		return domainUri;
	}
	public void setDomainUri(String domainUri) {
		this.domainUri = domainUri;
	}
	public String getLogoUri() {
		return logoUri;
	}
	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public String getFilingNumber() {
		return filingNumber;
	}
	public void setFilingNumber(String filingNumber) {
		this.filingNumber = filingNumber;
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
	public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getCreateDesc() {
		return createDesc;
	}
	public void setCreateDesc(String createDesc) {
		this.createDesc = createDesc;
	}
}
