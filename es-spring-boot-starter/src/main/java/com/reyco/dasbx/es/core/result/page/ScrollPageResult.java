package com.reyco.dasbx.es.core.result.page;

public class ScrollPageResult implements PageResult {

	/**
	 * scrollId
	 */
	private String scrollId;

	/**
	 * size
	 */
	private Integer pageSize;

	public String getScrollId() {
		return scrollId;
	}

	public void setScrollId(String scrollId) {
		this.scrollId = scrollId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
