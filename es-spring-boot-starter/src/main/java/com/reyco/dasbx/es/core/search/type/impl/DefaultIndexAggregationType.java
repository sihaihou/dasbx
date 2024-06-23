package com.reyco.dasbx.es.core.search.type.impl;

import com.reyco.dasbx.es.core.search.type.IndexAggregationType;

public class DefaultIndexAggregationType implements IndexAggregationType {
	private String indexName;
	private String[] aggregationFields;
	private String[] aggregationNames;
	private Integer[] aggregationSizes;
	private Boolean[] aggregationFieldOrders;
	@Override
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	@Override
	public String[] getAggregationFields() {
		return aggregationFields;
	}
	public void setAggregationFields(String[] aggregationFields) {
		this.aggregationFields = aggregationFields;
	}
	@Override
	public String[] getAggregationNames() {
		return aggregationNames;
	}
	public void setAggregationNames(String[] aggregationNames) {
		this.aggregationNames = aggregationNames;
	}
	@Override
	public Integer[] getAggregationSizes() {
		return aggregationSizes;
	}
	public void setAggregationSizes(Integer[] aggregationSizes) {
		this.aggregationSizes = aggregationSizes;
	}
	@Override
	public Boolean[] getAggregationFieldOrders() {
		return aggregationFieldOrders;
	}
	public void setAggregationFieldOrders(Boolean[] aggregationFieldOrders) {
		this.aggregationFieldOrders = aggregationFieldOrders;
	}
}
