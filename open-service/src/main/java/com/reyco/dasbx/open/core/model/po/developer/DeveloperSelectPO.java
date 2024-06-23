package com.reyco.dasbx.open.core.model.po.developer;

import com.reyco.dasbx.model.po.SimpleSelectPO;

public class DeveloperSelectPO extends SimpleSelectPO{
	private String name;
	private Byte type;
	private Byte state;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
}
