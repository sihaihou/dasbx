package com.reyco.dasbx.portal.model.domain.dto;

import java.math.BigDecimal;

import com.reyco.dasbx.model.dto.SimpleInsertDto;

public class VideoDanmakuInsertDto extends SimpleInsertDto{
	private Long videoId;
	private Long userId;
	private String content;
	private BigDecimal danmakuTime;
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public BigDecimal getDanmakuTime() {
		return danmakuTime;
	}
	public void setDanmakuTime(BigDecimal danmakuTime) {
		this.danmakuTime = danmakuTime;
	}
}
