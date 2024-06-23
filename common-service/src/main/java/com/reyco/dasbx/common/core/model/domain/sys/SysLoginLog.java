package com.reyco.dasbx.common.core.model.domain.sys;

import com.reyco.dasbx.model.Base;

public class SysLoginLog extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8406112603590782047L;
	private Long code;
	private Long userId;
	private String username;
	private String loginDevice;
	private String logoutDevice;
	private String loginIp;
	private String logoutIp;
	private String loginCity;
	private String logoutCity;
	private Long gmtLogin;
	private Long gmtLogout;
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
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
	public String getLoginDevice() {
		return loginDevice;
	}
	public void setLoginDevice(String loginDevice) {
		this.loginDevice = loginDevice;
	}
	public String getLogoutDevice() {
		return logoutDevice;
	}
	public void setLogoutDevice(String logoutDevice) {
		this.logoutDevice = logoutDevice;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLogoutIp() {
		return logoutIp;
	}
	public void setLogoutIp(String logoutIp) {
		this.logoutIp = logoutIp;
	}
	public String getLoginCity() {
		return loginCity;
	}
	public void setLoginCity(String loginCity) {
		this.loginCity = loginCity;
	}
	public String getLogoutCity() {
		return logoutCity;
	}
	public void setLogoutCity(String logoutCity) {
		this.logoutCity = logoutCity;
	}
	public Long getGmtLogin() {
		return gmtLogin;
	}
	public void setGmtLogin(Long gmtLogin) {
		this.gmtLogin = gmtLogin;
	}
	public Long getGmtLogout() {
		return gmtLogout;
	}
	public void setGmtLogout(Long gmtLogout) {
		this.gmtLogout = gmtLogout;
	}
}
