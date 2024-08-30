package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.model.dto.SimpleInsertDto;

public class VideoInsertDto extends SimpleInsertDto {
	private String name;
	private Long categoryId;
	private Long countryId;
	private Long typeId;
	private Long yearId;
	private Long vipId;
	private String sourceUrl;
	private String portraitCoverUrl;
	private String landscapeCoverUrl;
	private String description;
	private VideoProductionInsertDto videoProductionInsertDto;
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
	public VideoProductionInsertDto getVideoProductionInsertDto() {
		return videoProductionInsertDto;
	}
	public void setVideoProductionInsertDto(VideoProductionInsertDto videoProductionInsertDto) {
		this.videoProductionInsertDto = videoProductionInsertDto;
	}
}
