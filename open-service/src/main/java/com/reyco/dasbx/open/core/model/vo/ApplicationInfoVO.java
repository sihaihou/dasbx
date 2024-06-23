package com.reyco.dasbx.open.core.model.vo;

import java.util.Date;

import com.reyco.dasbx.model.vo.InfoVO;

public class ApplicationInfoVO implements InfoVO{
	private Long id;
	private Long firstCategoryId;
	private String firstCategoryDesc;
	private Long secondCategoryId;
	private String secondCategoryDesc;
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
	private Date gmtCreate;
	private String createDesc;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFirstCategoryId() {
		return firstCategoryId;
	}
	public void setFirstCategoryId(Long firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}
	public String getFirstCategoryDesc() {
		return firstCategoryDesc;
	}
	public void setFirstCategoryDesc(String firstCategoryDesc) {
		this.firstCategoryDesc = firstCategoryDesc;
	}
	public Long getSecondCategoryId() {
		return secondCategoryId;
	}
	public void setSecondCategoryId(Long secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}
	public String getSecondCategoryDesc() {
		return secondCategoryDesc;
	}
	public void setSecondCategoryDesc(String secondCategoryDesc) {
		this.secondCategoryDesc = secondCategoryDesc;
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
