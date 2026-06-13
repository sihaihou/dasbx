package com.reyco.dasbx.common.core.model.vo.personage;

import java.math.BigDecimal;

import com.reyco.dasbx.es.support.result.record.EsGeoDistanceResult;
import com.reyco.dasbx.es.support.result.record.EsHighlightResult;
import com.reyco.dasbx.model.es.GeoPoint;
import com.reyco.dasbx.model.vo.InfoVO;

public class PersonageInfoVO implements InfoVO {
	
	private Long id;
	
	@EsHighlightResult
	private String name;
	@EsHighlightResult
	private String code;
	@EsHighlightResult
	private String masterpiece;
	
	@EsGeoDistanceResult
	private Double distance;
	
	private Byte gender;
	private Long provinceId;
	private String provinceDesc;
	private Long cityId;
	private String cityDesc;
	private Long districtId;
	private String districtDesc;
	private BigDecimal longitude; 
	private BigDecimal latitude;
	private GeoPoint location;
	private String remark;
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
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public String getDistrictDesc() {
		return districtDesc;
	}
	public void setDistrictDesc(String districtDesc) {
		this.districtDesc = districtDesc;
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
	public GeoPoint getLocation() {
		return location;
	}
	public void setLocation(GeoPoint location) {
		this.location = location;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
