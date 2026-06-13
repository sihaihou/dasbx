package com.reyco.dasbx.es.core.feature.aggregation;

import java.util.ArrayList;
import java.util.List;

public class AggregationNode {
	/**
	 * 名称
	 */
	private String name;
	/**
	 * aggregation type
	 */
	private AggregationType type;

	/**
	 * bucket key
	 */
	private Object key;

	/**
	 * doc count
	 */
	private Long docCount;

	/**
	 * metric value
	 */
	private Object value;

	/**
	 * 子节点
	 */
	private List<AggregationNode> children = new ArrayList<>();

	public AggregationNode addChild(AggregationNode child) {
		children.add(child);
		return this;
	}

	public String getName() {
		return name;
	}

	public AggregationNode setName(String name) {
		this.name = name;
		return this;
	}
	
	public AggregationType getType() {
		return type;
	}
	public AggregationNode setType(AggregationType type) {
		this.type = type;
		return this;
	}
	
	public Object getKey() {
		return key;
	}

	public AggregationNode setKey(Object key) {
		this.key = key;
		return this;
	}

	public Long getDocCount() {
		return docCount;
	}

	public AggregationNode setDocCount(Long docCount) {
		this.docCount = docCount;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public AggregationNode setValue(Object value) {
		this.value = value;
		return this;
	}

	public List<AggregationNode> getChildren() {
		return children;
	}
}
