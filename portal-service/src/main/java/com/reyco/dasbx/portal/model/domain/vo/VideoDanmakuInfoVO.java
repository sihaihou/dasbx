package com.reyco.dasbx.portal.model.domain.vo;

import java.math.BigDecimal;

import com.reyco.dasbx.model.vo.InfoVO;

public class VideoDanmakuInfoVO implements InfoVO {
	private Long id;
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
