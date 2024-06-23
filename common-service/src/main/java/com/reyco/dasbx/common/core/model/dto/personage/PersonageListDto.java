package com.reyco.dasbx.common.core.model.dto.personage;

import com.reyco.dasbx.model.dto.SimpleListDto;

public class PersonageListDto extends SimpleListDto{
	private String keyword;
	private Integer gender;
	private Long provinceId;
	private Long cityId;
	private Long districtId;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
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
}
