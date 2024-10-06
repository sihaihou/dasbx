package com.reyco.dasbx.portal.model.domain;

import com.reyco.dasbx.commons.utils.serializable.ToString;

public class Category extends ToString {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4582046425464576820L;
	private Long id;
	private String name;
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
}
