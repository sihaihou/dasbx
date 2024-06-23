package com.reyco.dasbx.login.core.model.dto;

import com.reyco.dasbx.model.dto.BaseDto;

public class ConfirmQRcodeDto implements BaseDto{
	private String qrcode;
	private String token;
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
