package com.reyco.dasbx.model.domain;

import java.math.BigDecimal;

import com.reyco.dasbx.model.Base;

/**
 * 人物、名流
 * @author reyco
 *
 */
public class Personage extends Base{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8517285868948771714L;
	private String name;
	/**
	 * code
	 */
	private String code;
	/**
	 * 性别
	 */
	private int gender;
	/**
	 * 代表作
	 */
	private String masterpiece;
	/**
	 * 省
	 */
	private Long provinceId;
	/**
	 * 市
	 */
	private Long cityId;
	/**
	 * 区
	 */
	private Long districtId;
	/**
	 * 经度
	 */
	private BigDecimal longitude; 
	/**
	 * 纬度
	 */
	private BigDecimal latitude; 
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
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getMasterpiece() {
		return masterpiece;
	}
	public void setMasterpiece(String masterpiece) {
		this.masterpiece = masterpiece;
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
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
}
