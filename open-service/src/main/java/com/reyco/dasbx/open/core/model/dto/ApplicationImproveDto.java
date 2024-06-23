package com.reyco.dasbx.open.core.model.dto;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

/**
 * 完善应用信息
 * @author reyco
 *
 */
public class ApplicationImproveDto extends SimpleUpdateDto{
	private String domainUri;
	private String redirectUri;
	private String organizer;
	private String filingNumber;
	private String logoUri;
	public String getDomainUri() {
		return domainUri;
	}
	public void setDomainUri(String domainUri) {
		this.domainUri = domainUri;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
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
	public String getLogoUri() {
		return logoUri;
	}
	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}
}
