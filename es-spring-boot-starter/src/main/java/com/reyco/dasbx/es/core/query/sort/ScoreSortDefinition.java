package com.reyco.dasbx.es.core.query.sort;

import org.elasticsearch.search.sort.SortOrder;

public class ScoreSortDefinition implements SortDefinition {
	
	private SortOrder sortOrder = SortOrder.DESC;
	
	private int order;
	
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	public ScoreSortDefinition setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}
	@Override
	public int getOrder() {
		return order;
	}
	public ScoreSortDefinition setOrder(int order) {
		this.order = order;
		return this;
	}
}
