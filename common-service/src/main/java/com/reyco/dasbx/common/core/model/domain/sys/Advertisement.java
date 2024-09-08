package com.reyco.dasbx.common.core.model.domain.sys;

import com.reyco.dasbx.model.Base;

/**
 * 广告对象
 * @author reyco
 *
 */
public class Advertisement extends Base{
	private Long id;
	private String coverUrl;
	private String linkUrl;
	private Byte type;
	private Long gmtSign;
	private Long gmtExpiration;
	private Integer transactionAmount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Long getGmtSign() {
		return gmtSign;
	}
	public void setGmtSign(Long gmtSign) {
		this.gmtSign = gmtSign;
	}
	public Long getGmtExpiration() {
		return gmtExpiration;
	}
	public void setGmtExpiration(Long gmtExpiration) {
		this.gmtExpiration = gmtExpiration;
	}
	public Integer getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Integer transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
}
