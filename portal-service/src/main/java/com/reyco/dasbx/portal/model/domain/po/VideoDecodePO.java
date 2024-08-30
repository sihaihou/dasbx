package com.reyco.dasbx.portal.model.domain.po;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class VideoDecodePO extends SimpleUpdatePO{
	private String playUrl;
	private Byte hls;
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public Byte getHls() {
		return hls;
	}
	public void setHls(Byte hls) {
		this.hls = hls;
	}
}
