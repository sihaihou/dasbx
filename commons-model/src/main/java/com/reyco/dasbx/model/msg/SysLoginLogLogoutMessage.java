package com.reyco.dasbx.model.msg;

import com.reyco.dasbx.commons.utils.ToString;

public class SysLoginLogLogoutMessage extends ToString  implements RabbitMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9053094462786306745L;
	private Long code;
	private String logoutDevice;
	private String logoutIp;
	private String logoutCity;
	private Long gmtLogout;
	private Long gmtModified;
	private Long modifiedBy;
	@Override
	public String getCorrelationDataId() {
		return code.toString();
	}
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
