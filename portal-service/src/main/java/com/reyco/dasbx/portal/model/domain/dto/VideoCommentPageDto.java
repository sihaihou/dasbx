package com.reyco.dasbx.portal.model.domain.dto;

import com.reyco.dasbx.model.dto.SimplePageDto;

public class VideoCommentPageDto extends SimplePageDto{
	private Long videoId;
	private Long parentId;
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}
