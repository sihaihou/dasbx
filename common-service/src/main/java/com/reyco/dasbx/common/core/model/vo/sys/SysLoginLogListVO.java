package com.reyco.dasbx.common.core.model.vo.sys;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.vo.BaseVO;

public class SysLoginLogListVO implements BaseVO{
	private Long id;
	private Long userId;
	private String username;
	private String loginDevice;
	private String loginIp;
	private String loginCity;
	private String remark;
	//登录时间
	private Long gmtLogin;
	private String gmtLoginDesc;
	//登出时间
	private Long gmtLogout;
	private String gmtLogoutDesc;
	//登出操作人
	private Long modifiedBy;
	private String modifiedByDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getGmtLogin() {
		return gmtLogin;
	}
	public void setGmtLogin(Long gmtLogin) {
		this.gmtLogin = gmtLogin;
		if(gmtLogin!=null) {
			this.gmtLoginDesc = Dasbx.getDateByTimeZone(gmtLogin);
		}
	}
	public String getGmtLoginDesc() {
		return gmtLoginDesc;
	}
	public void setGmtLoginDesc(String gmtLoginDesc) {
		this.gmtLoginDesc = gmtLoginDesc;
	}
	public Long getGmtLogout() {
		return gmtLogout;
	}
	public void setGmtLogout(Long gmtLogout) {
		this.gmtLogout = gmtLogout;
		if(gmtLogout!=null) {
			this.gmtLogoutDesc = Dasbx.getDateByTimeZone(gmtLogout);
		}
	}
	public String getGmtLogoutDesc() {
		return gmtLogoutDesc;
	}
	public void setGmtLogoutDesc(String gmtLogoutDesc) {
		this.gmtLogoutDesc = gmtLogoutDesc;
	}
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedByDesc() {
		return modifiedByDesc;
	}
	public void setModifiedByDesc(String modifiedByDesc) {
		this.modifiedByDesc = modifiedByDesc;
	}
}
