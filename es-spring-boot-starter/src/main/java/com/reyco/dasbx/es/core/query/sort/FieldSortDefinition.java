package com.reyco.dasbx.es.core.query.sort;

import org.elasticsearch.search.sort.SortOrder;

public class FieldSortDefinition implements SortDefinition {

	private String field;
	/**
	 * 解析后字段
	 */
	private String resolvedField;
	
	private SortOrder sortOrder = SortOrder.DESC;
	
	private int order;

	public String getField() {
		return field;
	}

	public FieldSortDefinition setField(String field) {
		this.field = field;
		return this;
	}

	public String getResolvedField() {
		return resolvedField;
	}

	public FieldSortDefinition setResolvedField(String resolvedField) {
		this.resolvedField = resolvedField;
		return this;
	}
		
	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public FieldSortDefinition setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}
	public FieldSortDefinition setOrder(int order) {
		this.order = order;
		return this;
	}
	@Override
	public int getOrder() {
		return order;
	}
}
