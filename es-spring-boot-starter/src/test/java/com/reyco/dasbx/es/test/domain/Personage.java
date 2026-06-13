package com.reyco.dasbx.es.test.domain;

import java.io.Serializable;

public class Personage implements Serializable{
	private Long id;
	public String name;
	private String code;
	private String masterpiece;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMasterpiece() {
		return masterpiece;
	}
	public void setMasterpiece(String masterpiece) {
		this.masterpiece = masterpiece;
	}
	@Override
	public String toString() {
		return "Personage [id=" + id + ", name=" + name + ", code=" + code + ", masterpiece=" + masterpiece + "]";
	}
}
