package com.reyco.dasbx.login.core.model.dto;

import com.reyco.dasbx.model.dto.BaseDto;

public class ScanQRCodeDto implements BaseDto {
	private Byte type;
	private String qrcode;
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	
}
