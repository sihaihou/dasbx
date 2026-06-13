package com.reyco.dasbx.commons.utils;

import java.util.Arrays;
import java.util.List;

public class Page<T> {
	private List<T> list;
	private Integer pageNum;
	private Integer pageSize;
	private Integer pages;
	private Integer total;
	//scrollId
	private String scrollId;
	/**
	 * 下一页游标
	 */
	private Object[] nextSearchAfter;
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
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
		if(total!=null) {
			this.pages = (total%pageSize)==0?(total/pageSize):(total/pageSize)+1;
		}
	}
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
		if(pageSize!=null) {
			this.pages = (total%pageSize)==0?(total/pageSize):(total/pageSize)+1;
		}
	}
	public String getScrollId() {
		return scrollId;
	}
	public void setScrollId(String scrollId) {
		this.scrollId = scrollId;
	}
	public Object[] getNextSearchAfter() {
		return nextSearchAfter;
	}
	public void setNextSearchAfter(Object[] nextSearchAfter) {
		this.nextSearchAfter = nextSearchAfter;
	}
	@Override
	public String toString() {
		return "Page [list=" + list + ", pageNum=" + pageNum + ", pageSize=" + pageSize + ", pages=" + pages
				+ ", total=" + total + ", scrollId=" + scrollId + ", nextSearchAfter="
				+ Arrays.toString(nextSearchAfter) + "]";
	}
}
