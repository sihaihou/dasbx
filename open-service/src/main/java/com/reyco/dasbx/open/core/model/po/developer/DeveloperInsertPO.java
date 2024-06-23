package com.reyco.dasbx.open.core.model.po.developer;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class DeveloperInsertPO extends SimpleInsertPO {
	private Byte type;
	private String name;
	private Long provinceId;
	private Long cityId;
	private String address;
	private String director;
	private String phone;
	private String email;
	private String idcard;
	private String faceUri;
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
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getFaceUri() {
		return faceUri;
	}
	public void setFaceUri(String faceUri) {
		this.faceUri = faceUri;
	}
}
