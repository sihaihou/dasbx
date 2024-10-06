package com.reyco.dasbx.es.core.search;

import org.elasticsearch.search.sort.SortOrder;

import com.reyco.dasbx.commons.utils.serializable.ToString;

public class Sort extends ToString{
	/**
	 * 
	 */
	private static final long serialVersionUID = -394807777426664123L;
	private String field;
	private SortOrder sortOrder;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
}
