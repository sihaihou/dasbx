package com.reyco.dasbx.common.core.model.dto.sys;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class SysLoginLogLogoutUpdateDto extends SimpleUpdateDto{
	private Long code;
	private String logoutDevice;
	private String logoutIp;
	private String logoutCity;
	private Long gmtLogout;
	private Long gmtModified;
	private Long modifiedBy;
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
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
		return logoutCity;
	}
	public void setLogoutCity(String logoutCity) {
		this.logoutCity = logoutCity;
	}
	public Long getGmtLogout() {
		return gmtLogout;
	}
	public void setGmtLogout(Long gmtLogout) {
		this.gmtLogout = gmtLogout;
	}
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
	}
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
