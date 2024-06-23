package com.reyco.dasbx.portal.model.domain.dto;

import java.math.BigDecimal;

import com.reyco.dasbx.model.dto.SimpleListDto;

public class VideoDanmakuListDto extends SimpleListDto {
	
	private Long videoId;
	private BigDecimal startDanmakuTime;
	private BigDecimal endDanmakuTime;
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public BigDecimal getStartDanmakuTime() {
		return startDanmakuTime;
	}
	public void setStartDanmakuTime(BigDecimal startDanmakuTime) {
		this.startDanmakuTime = startDanmakuTime;
	}
	public BigDecimal getEndDanmakuTime() {
		return endDanmakuTime;
	}
	public void setEndDanmakuTime(BigDecimal endDanmakuTime) {
		this.endDanmakuTime = endDanmakuTime;
	}
}
