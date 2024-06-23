package com.reyco.dasbx.portal.model.domain;

import java.math.BigDecimal;

import com.reyco.dasbx.model.Base;

public class VideoDanmaku extends Base {
	/**
	 * 
	 */
	private static long serialVersionUID = -6902656896923810602L;
	private Long videoId;
	private Long userId;
	private String content;
	private BigDecimal danmakuTime;
	private Long time;
	private String ip;
	private String province;
	private String city;
	private String district;
	private String address;
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
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
