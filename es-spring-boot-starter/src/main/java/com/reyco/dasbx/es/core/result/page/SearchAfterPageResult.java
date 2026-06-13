package com.reyco.dasbx.es.core.result.page;

public class SearchAfterPageResult implements PageResult {

	/**
	 * size
	 */
	private Integer pageSize;

	/**
	 * 下一页游标
	 */
	private Object[] nextSearchAfter;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Object[] getNextSearchAfter() {
		return nextSearchAfter;
	}

	public void setNextSearchAfter(Object[] nextSearchAfter) {
		this.nextSearchAfter = nextSearchAfter;
	}
}
