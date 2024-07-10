package com.reyco.dasbx.common.core.model.dto.personage;

import com.reyco.dasbx.es.core.search.SimpleSearchDto;

public class PersonageSearchDto extends SimpleSearchDto {
	private Byte gender;
	private String masterpiece;
	private Double longitude; 
	private Double latitude; 
	private Integer startAge;
	private Integer endAge;
	public Byte getGender() {
		return gender;
	}
	public void setGender(Byte gender) {
		this.gender = gender;
	}
	public String getMasterpiece() {
		return masterpiece;
	}
	public void setMasterpiece(String masterpiece) {
		this.masterpiece = masterpiece;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Integer getStartAge() {
		return startAge;
	}
	public void setStartAge(Integer startAge) {
		this.startAge = startAge;
	}
	public Integer getEndAge() {
		return endAge;
	}
	public void setEndAge(Integer endAge) {
		this.endAge = endAge;
	}
}
