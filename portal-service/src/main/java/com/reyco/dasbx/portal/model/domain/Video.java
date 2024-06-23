package com.reyco.dasbx.portal.model.domain;

import com.reyco.dasbx.model.Base;

public class Video extends Base {
	/**
	 * 
	 */
	private static long serialVersionUID = 51319128102818491L;
	private String name;
	private Long categoryId;
	private Long countryId;
	private String countryName;
	private Long typeId;
	private String typeName;
	private Long yearId;
	private String yearName;
	private Long vipId;
	private Long accountId;
	private String playUrl;
	private String portraitCoverUrl;
	private String landscapeCoverUrl;
	private String description;
	private Integer playQuantity;
	private Integer commentQuantity;
	private Integer collectionQuantity;
	private Integer shareQuantity;
	private Integer heatQuantity;
	private Byte hls;
	private Byte state;
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public static void setSerialVersionUID(long serialVersionUID) {
		Video.serialVersionUID = serialVersionUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Long getYearId() {
		return yearId;
	}
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}
	public String getYearName() {
		return yearName;
	}
	public void setYearName(String yearName) {
		this.yearName = yearName;
	}
	public Long getVipId() {
		return vipId;
	}
	public void setVipId(Long vipId) {
		this.vipId = vipId;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public String getPortraitCoverUrl() {
		return portraitCoverUrl;
	}
	public void setPortraitCoverUrl(String portraitCoverUrl) {
		this.portraitCoverUrl = portraitCoverUrl;
	}
	public String getLandscapeCoverUrl() {
		return landscapeCoverUrl;
	}
	public void setLandscapeCoverUrl(String landscapeCoverUrl) {
		this.landscapeCoverUrl = landscapeCoverUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPlayQuantity() {
		return playQuantity;
	}
	public void setPlayQuantity(Integer playQuantity) {
		this.playQuantity = playQuantity;
	}
	public Integer getCommentQuantity() {
		return commentQuantity;
	}
	public void setCommentQuantity(Integer commentQuantity) {
		this.commentQuantity = commentQuantity;
	}
	public Integer getCollectionQuantity() {
		return collectionQuantity;
	}
	public void setCollectionQuantity(Integer collectionQuantity) {
		this.collectionQuantity = collectionQuantity;
	}
	public Integer getShareQuantity() {
		return shareQuantity;
	}
	public void setShareQuantity(Integer shareQuantity) {
		this.shareQuantity = shareQuantity;
	}
	public Integer getHeatQuantity() {
		return heatQuantity;
	}
	public void setHeatQuantity(Integer heatQuantity) {
		this.heatQuantity = heatQuantity;
	}
	public Byte getHls() {
		return hls;
	}
	public void setHls(Byte hls) {
		this.hls = hls;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
}
