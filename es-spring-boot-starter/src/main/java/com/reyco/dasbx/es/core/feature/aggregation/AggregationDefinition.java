package com.reyco.dasbx.es.core.feature.aggregation;

import java.util.ArrayList;
import java.util.List;

public class AggregationDefinition {
	/**
	 * 聚合名称
	 */
	private String name;

	/**
	 * 聚合字段
	 */
	private String field;
	/**
	 * 解析后field
	 */
	private String resolvedField;
	/**
	 * 
	 */
	private Integer size = 10;
	
	private Boolean order = true;
	/**
	 * 
	 */
	private AggregationType type;
	
	private String interval;
	/**
	 * 子聚合
	 */
	private List<AggregationDefinition> children = new ArrayList<>();
	
	public AggregationDefinition() {
	}
	
	public AggregationDefinition addChild(AggregationDefinition agg) {
		children.add(agg);
		return this;
	}
	public List<AggregationDefinition> getChildren() {
		return children;
	}
	
	public String getName() {
		return name;
	}

	public AggregationDefinition setName(String name) {
		this.name = name;
		return this;
	}

	public String getField() {
		return field;
	}

	public AggregationDefinition setField(String field) {
		this.field = field;
		return this;
	}
	public Integer getSize() {
		return size;
	}
	public AggregationDefinition setSize(Integer size) {
		this.size = size;
		return this;
	}
	public Boolean getOrder() {
		return order;
	}

	public AggregationDefinition setOrder(Boolean order) {
		this.order = order;
		return this;
	}

	public AggregationType getType() {
		return type;
	}
	public AggregationDefinition setType(AggregationType type) {
		this.type = type;
		return this;
	}
	public String getInterval() {
		return interval;
	}
	public AggregationDefinition setInterval(String interval) {
		this.interval = interval;
		return this;
	}
	public String getResolvedField() {
		return resolvedField;
	}
	public void setResolvedField(String resolvedField) {
		this.resolvedField = resolvedField;
	}
}
