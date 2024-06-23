package com.reyco.dasbx.model.domain;

import com.reyco.dasbx.model.Base;

public class Name extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 111672810449961809L;
	private String name;
	private Boolean gender;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
}
