package com.reyco.dasbx.open.core.model.vo.developer;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.vo.InfoVO;

public class DeveloperInfoVO implements InfoVO {
	private Long id;
	private Byte type;
	private String name;
	private Long provinceId;
	private String provinceDesc;
	private Long cityId;
	private String cityDesc;
	private String address;
	private String director;
	private String phone;
	private String email;
	private String idcard;
	private String faceUri;
	private Byte state;
	private String stateDesc;
	private Long gmtCreate;
	private String gmtCreateDesc;
	private Long gmtModified;
	private String gmtModifiedDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceDesc() {
		return provinceDesc;
	}
	public void setProvinceDesc(String provinceDesc) {
		this.provinceDesc = provinceDesc;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getCityDesc() {
		return cityDesc;
	}
	public void setCityDesc(String cityDesc) {
		this.cityDesc = cityDesc;
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
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public String getStateDesc() {
		return stateDesc;
	}
	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}
	public Long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
		if(this.gmtCreate!=null) {
			this.gmtCreateDesc = Dasbx.getDateByTimeZone(gmtCreate);
		}
	}
	public String getGmtCreateDesc() {
		return gmtCreateDesc;
	}
	public void setGmtCreateDesc(String gmtCreateDesc) {
		this.gmtCreateDesc = gmtCreateDesc;
	}
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
		if(this.gmtModified!=null) {
			this.gmtModifiedDesc = Dasbx.getDateByTimeZone(gmtModified);
		}
	}
	public String getGmtModifiedDesc() {
		return gmtModifiedDesc;
	}
	public void setGmtModifiedDesc(String gmtModifiedDesc) {
		this.gmtModifiedDesc = gmtModifiedDesc;
	}
}
