package com.reyco.dasbx.es.core.search;

import org.elasticsearch.search.sort.SortOrder;

public class SimpleSearchAfterDto extends SimpleSearchDto implements SearchAfterDto{
	
	public final static String DEFALUT_SORT_FIELD = "_id";
	
	public final static SortOrder DEFALUT_SORT_ORDER = SortOrder.ASC;
	
	public final static String DEFALUT_SORT_ID = "0";
	
	/**
	 * sort字段：默认 '_id'
	 */
	private String sortField = DEFALUT_SORT_FIELD;
	/**
	 * 排序方式：默认升序ASC
	 */
	private SortOrder sortOrder = DEFALUT_SORT_ORDER;
	/**
	 * sortId
	 */
	private String sortId = DEFALUT_SORT_ID;
	@Override
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	@Override
	public SortOrder getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
	@Override
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
}
