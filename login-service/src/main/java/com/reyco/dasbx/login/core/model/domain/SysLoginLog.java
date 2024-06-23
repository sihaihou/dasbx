package com.reyco.dasbx.login.core.model.domain;

import com.reyco.dasbx.model.Base;

public class SysLoginLog extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6147463947247944169L;
	private Long userId;
	private String username;
	private String device;
	private String city;
	private String ip;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
