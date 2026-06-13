package com.reyco.dasbx.es.core.result.page;

public class OffsetPageDefinition implements PageDefinition {

	/**
	 * 页码
	 */
	private Integer pageNum = Pages.DEFAULT_PAGE_NUM;

	/**
	 * 分页大小
	 */
	private Integer pageSize = Pages.DEFAULT_PAGE_SIZE;

	public Integer getPageNum() {
		return pageNum;
	}

	public OffsetPageDefinition setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public OffsetPageDefinition setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
}
