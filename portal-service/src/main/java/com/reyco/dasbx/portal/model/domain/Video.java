package com.reyco.dasbx.portal.model.domain;

import java.math.BigDecimal;

import com.reyco.dasbx.model.Base;

public class Video extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8758060908175977906L;
	private String name;
	private String description;
	private Long categoryId;
	private Long countryId;
	private Long typeId;
	private Long yearId;
	private Long vipId;
	private String sourceUrl;
	private String playUrl;
	private String portraitCoverUrl;
	private String landscapeCoverUrl;
	private BigDecimal score;
	private Integer playQuantity;
	private Integer commentQuantity;
	private Integer collectionQuantity;
	private Integer shareQuantity;
	private Integer heatQuantity;
	private Byte hls;
	private Byte state;
	private Long uploadBy;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getYearId() {
		return yearId;
	}
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}
	public Long getVipId() {
		return vipId;
	}
	public void setVipId(Long vipId) {
		this.vipId = vipId;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
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
	public BigDecimal getScore() {
		return score;
	}
	public void setScore(BigDecimal score) {
		this.score = score;
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
	public Long getUploadBy() {
		return uploadBy;
	}
	public void setUploadBy(Long uploadBy) {
		this.uploadBy = uploadBy;
	}
}
