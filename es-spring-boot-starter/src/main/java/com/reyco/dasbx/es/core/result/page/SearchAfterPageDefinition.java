package com.reyco.dasbx.es.core.result.page;

public class SearchAfterPageDefinition implements PageDefinition {

	/**
	 * size
	 */
	private Integer pageSize = 10;

	/**
	 * 游标
	 */
	private Object[] searchAfter;

	public Integer getPageSize() {
		return pageSize;
	}

	public SearchAfterPageDefinition setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public Object[] getSearchAfter() {
		return searchAfter;
	}

	public SearchAfterPageDefinition setSearchAfter(Object[] searchAfter) {
		this.searchAfter = searchAfter;
		return this;
	}
}
