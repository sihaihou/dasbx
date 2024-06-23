package com.reyco.dasbx.model.constants;

public enum SysLoginLogType {
	LOGIN("登录",(byte)0),
	LOGOUT("登出",(byte)1);
	private String name;
	private Byte type;
	
	SysLoginLogType(String name,Byte type) {
		this.name = name;
		this.type = type;
	}
	public static SysLoginLogType getSysLoginLogType(Byte type) {
		for (SysLoginLogType sysLoginLogType : SysLoginLogType.values()) {
			if (sysLoginLogType.type == type) {
				return sysLoginLogType;
			}
		}
		return null;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}