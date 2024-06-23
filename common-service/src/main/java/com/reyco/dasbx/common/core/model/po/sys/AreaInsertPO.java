package com.reyco.dasbx.common.core.model.po.sys;

import java.util.Set;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class AreaInsertPO extends SimpleInsertPO {
	private Long parentId;
	private String name;
	private String code;
	private String citycode;
	private String level;
	private Byte leaf;
	private String longitude;
	private String latitude;
	private String remark;
	private Set<AreaInsertPO> areaInsertPOs;
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
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Set<AreaInsertPO> getAreaInsertPOs() {
		return areaInsertPOs;
	}
	public void setAreaInsertPOs(Set<AreaInsertPO> areaInsertPOs) {
		this.areaInsertPOs = areaInsertPOs;
	}
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AreaInsertPO)) {
			return false;
		}
		AreaInsertPO areaInsertPO = (AreaInsertPO)obj;
		return this.name.equals(areaInsertPO.name);
	}
}
