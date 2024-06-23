package com.reyco.dasbx.portal.model.domain.vo;

import java.math.BigDecimal;

import com.reyco.dasbx.model.vo.ListVO;

public class VideoDanmakuListVO implements ListVO{
	private Long id;
	private Long videoId;
	private Long userId;
	private String content;
	private BigDecimal danmakuTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
