package com.reyco.dasbx.model.domain;

import com.reyco.dasbx.commons.utils.ToString;

public class Fullname extends ToString{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7189352257232315702L;
	private Surname surname;
	private Name name;
	private String fullname;
	private Boolean gender;
	public Surname getSurname() {
		return surname;
	}
	public void setSurname(Surname surname) {
		this.surname = surname;
	}
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
}
