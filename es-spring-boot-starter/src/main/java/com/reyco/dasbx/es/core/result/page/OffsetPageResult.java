package com.reyco.dasbx.es.core.result.page;

public class OffsetPageResult implements PageResult {

	/**
	 * 页码
	 */
	private Integer pageNum;

	/**
	 * 分页大小
	 */
	private Integer pageSize;

	/**
	 * 总数
	 */
	private Long total;

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}
