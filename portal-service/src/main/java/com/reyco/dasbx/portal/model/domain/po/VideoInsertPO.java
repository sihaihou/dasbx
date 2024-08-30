package com.reyco.dasbx.portal.model.domain.po;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class VideoInsertPO extends SimpleInsertPO {
	private String name;
	private Long categoryId;
	private Long countryId;
	private String countryName;
	private Long typeId;
	private String typeName;
	private Long yearId;
	private String yearName;
	private Long vipId;
	private String playUrl;
	private String sourceUrl;
	private String portraitCoverUrl;
	private String landscapeCoverUrl;
	private String description;
	private Long uploadBy;
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
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
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
	public Long getUploadBy() {
		return uploadBy;
	}
	public void setUploadBy(Long uploadBy) {
		this.uploadBy = uploadBy;
	}
}
