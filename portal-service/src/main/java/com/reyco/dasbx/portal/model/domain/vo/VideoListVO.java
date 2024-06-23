package com.reyco.dasbx.portal.model.domain.vo;

import com.reyco.dasbx.model.vo.ListVO;

public class VideoListVO implements ListVO {
	private long id;
	private Long categoryId;
	private Long vipId;
	private String name;
	private String portraitCoverUrl;
	private String landscapeCoverUrl;
	private String description;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getVipId() {
		return vipId;
	}
	public void setVipId(Long vipId) {
		this.vipId = vipId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
}
