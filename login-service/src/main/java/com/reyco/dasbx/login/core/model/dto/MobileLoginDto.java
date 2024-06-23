package com.reyco.dasbx.login.core.model.dto;

/**
 * 手机登录
 * @author reyco
 *
 */
public class MobileLoginDto extends PasswordLoginDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 设备Id
	 */
	private String deviceId;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
