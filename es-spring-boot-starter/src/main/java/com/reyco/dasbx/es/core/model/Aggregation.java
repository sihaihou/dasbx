package com.reyco.dasbx.es.core.model;

import java.io.Serializable;

public class Aggregation implements Serializable{
	private Long id;
	private String value;
	public Aggregation() {
	}
	public Aggregation(String value) {
		super();
		this.value = value;
	}
	public Aggregation(Long id, String value) {
		super();
		this.id = id;
		this.value = value;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Aggregation [id=" + id + ", value=" + value + "]";
	}
}
