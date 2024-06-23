package com.reyco.dasbx.login.core.model.vo;

import com.reyco.dasbx.model.vo.InfoVO;

public class QRCodeInfoVO implements InfoVO{
	
	private String deviceId;
	private String qrcode;
	private Byte state;
	private String code;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
