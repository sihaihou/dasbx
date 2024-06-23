package com.reyco.dasbx.model.domain;

import java.math.BigDecimal;

import com.reyco.dasbx.model.Base;

public class Area extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6766218704178603957L;
	private Long parentId;
	private String name;
    private String code;
    private String citycode;
    private String level;
    private Byte leaf;
    private BigDecimal longitude;
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
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Area)) {
			return false;
		}
		Area area = (Area)obj;
		return this.getId().equals(area.getId());
	}
}
