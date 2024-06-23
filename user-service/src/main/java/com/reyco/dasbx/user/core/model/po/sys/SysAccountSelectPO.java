package com.reyco.dasbx.user.core.model.po.sys;

import com.reyco.dasbx.model.po.SimpleSelectPO;

public class SysAccountSelectPO extends SimpleSelectPO{
	private String username;
	private Byte gender;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Byte getGender() {
		return gender;
	}
	public void setGender(Byte gender) {
		this.gender = gender;
	}
}
