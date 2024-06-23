package com.reyco.dasbx.common.core.model.po.sys;

import com.reyco.dasbx.model.dto.SimpleListDto;

public class SysLoginLogPO extends SimpleListDto{
	private String username;
	private Long userId;
	private Long startLoginTime;
	private Long endLoginTime;
	private Long startLogoutTime;
	private Long endLogoutTime;
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
	public Long getStartLoginTime() {
		return startLoginTime;
	}
	public void setStartLoginTime(Long startLoginTime) {
		this.startLoginTime = startLoginTime;
	}
	public Long getEndLoginTime() {
		return endLoginTime;
	}
	public void setEndLoginTime(Long endLoginTime) {
		this.endLoginTime = endLoginTime;
	}
	public Long getStartLogoutTime() {
		return startLogoutTime;
	}
	public void setStartLogoutTime(Long startLogoutTime) {
		this.startLogoutTime = startLogoutTime;
	}
	public Long getEndLogoutTime() {
		return endLogoutTime;
	}
	public void setEndLogoutTime(Long endLogoutTime) {
		this.endLogoutTime = endLogoutTime;
	}
}
