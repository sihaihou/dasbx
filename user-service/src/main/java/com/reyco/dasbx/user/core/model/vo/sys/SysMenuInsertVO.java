package com.reyco.dasbx.user.core.model.vo.sys;

import com.reyco.dasbx.model.vo.InfoVO;

public class SysMenuInsertVO implements InfoVO {
	private Long id;
	private Long parentId;
    private String name;
    private String englishName;
    private String url;
    private String perms;
    private Integer type;
    private String icon;
    private Integer orderNum;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPerms() {
		return perms;
	}
	public void setPerms(String perms) {
		this.perms = perms;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
}
