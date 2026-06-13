package com.reyco.dasbx.es.support.annotation.page;

public class EsPageParam {
	/**
	 * 分页模式
	 */
	private PageMode mode = PageMode.OFFSET;

	/**
	 * pageNum
	 */
	private Integer pageNum = 1;

	/**
	 * pageSize
	 */
	private Integer pageSize = 10;

	/**
	 * search_after
	 */
	private Object[] searchAfter;

	/**
	 * scrollId
	 */
	private String scrollId;

	/**
	 * scroll keep alive
	 */
	private String scroll = "1m";

	public PageMode getMode() {
		return mode;
	}

	public EsPageParam setMode(PageMode mode) {
		this.mode = mode;
		return this;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public EsPageParam setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public EsPageParam setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public Object[] getSearchAfter() {
		return searchAfter;
	}

	public EsPageParam setSearchAfter(Object[] searchAfter) {
		this.searchAfter = searchAfter;
		return this;
	}

	public String getScrollId() {
		return scrollId;
	}

	public EsPageParam setScrollId(String scrollId) {
		this.scrollId = scrollId;
		return this;
	}

	public String getScroll() {
		return scroll;
	}

	public EsPageParam setScroll(String scroll) {
		this.scroll = scroll;
		return this;
	}
	
}
