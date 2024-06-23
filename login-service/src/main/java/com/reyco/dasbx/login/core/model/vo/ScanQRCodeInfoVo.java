package com.reyco.dasbx.login.core.model.vo;

import com.reyco.dasbx.model.vo.InfoVO;

public class ScanQRCodeInfoVo implements InfoVO{
	private byte type;
	private String token;
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
