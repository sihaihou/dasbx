package com.reyco.dasbx.common.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleUpdatePO;

public class SysLoginLogLoginUpdatePO extends SimpleUpdatePO {
	private String loginDevice;
	private String loginIp;
	private String loginCity;
	private Long gmtLogin;
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
}
