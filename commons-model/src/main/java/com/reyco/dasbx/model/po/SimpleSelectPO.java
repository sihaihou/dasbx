package com.reyco.dasbx.model.po;

public class SimpleSelectPO implements SelectPO{
	private Long id;
	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
