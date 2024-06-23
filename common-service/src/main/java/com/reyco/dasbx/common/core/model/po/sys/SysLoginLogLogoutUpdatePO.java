package com.reyco.dasbx.common.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class SysLoginLogLogoutUpdatePO extends SimpleUpdatePO {
	private String logoutDevice;
	private String logoutIp;
	private String LogoutCity;
	private Long gmtLogout;
	public String getLogoutDevice() {
		return logoutDevice;
	}
	public void setLogoutDevice(String logoutDevice) {
		this.logoutDevice = logoutDevice;
	}
	public String getLogoutIp() {
		return logoutIp;
	}
	public void setLogoutIp(String logoutIp) {
		this.logoutIp = logoutIp;
	}
	public String getLogoutCity() {
		return LogoutCity;
	}
	public void setLogoutCity(String logoutCity) {
		LogoutCity = logoutCity;
	}
	public Long getGmtLogout() {
		return gmtLogout;
	}
	public void setGmtLogout(Long gmtLogout) {
		this.gmtLogout = gmtLogout;
	}
}
