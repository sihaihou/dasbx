package com.reyco.dasbx.common.core.model.dto.sys;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class SysLoginLogLoginUpdateDto extends SimpleUpdateDto{
	private Long code;
	private String loginDevice;
	private String loginIp;
	private String loginCity;
	private Long gmtLogin;
	private Long gmtModified;
	private Long modifiedBy;
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getLoginDevice() {
		return loginDevice;
	}
	public void setLoginDevice(String loginDevice) {
		this.loginDevice = loginDevice;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginCity() {
		return loginCity;
	}
	public void setLoginCity(String loginCity) {
		this.loginCity = loginCity;
	}
	public Long getGmtLogin() {
		return gmtLogin;
	}
	public void setGmtLogin(Long gmtLogin) {
		this.gmtLogin = gmtLogin;
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
