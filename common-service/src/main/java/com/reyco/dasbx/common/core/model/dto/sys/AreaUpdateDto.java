package com.reyco.dasbx.common.core.model.dto.sys;

import java.math.BigDecimal;

import com.reyco.dasbx.model.dto.SimpleUpdateDto;

public class AreaUpdateDto extends SimpleUpdateDto {
	private Long parentId;
	private String name;
    private String code;
    private String citycode;
    private String level;
    private Byte leaf;
    private BigDecimal longitude;
    private BigDecimal latitude;
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Byte getLeaf() {
		return leaf;
	}
	public void setLeaf(Byte leaf) {
		this.leaf = leaf;
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
