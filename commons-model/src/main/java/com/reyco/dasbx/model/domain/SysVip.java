package com.reyco.dasbx.model.domain;

import com.reyco.dasbx.model.Base;

public class SysVip extends Base{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7112796547983022806L;
	private String name;
	private Integer lowVip;
	private Integer higVip;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLowVip() {
		return lowVip;
	}
	public void setLowVip(Integer lowVip) {
		this.lowVip = lowVip;
	}
	public Integer getHigVip() {
		return higVip;
	}
	public void setHigVip(Integer higVip) {
		this.higVip = higVip;
	}
	
}
