package com.reyco.dasbx.portal.model.domain.po;

import java.math.BigDecimal;

import com.reyco.dasbx.model.po.SimpleSelectPO;

public class VideoDanmakuListPO extends SimpleSelectPO {
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
