package com.reyco.dasbx.es.dto;

import com.reyco.dasbx.es.support.annotation.page.EsPage;
import com.reyco.dasbx.es.support.annotation.page.EsPageParam;

public class SearchPageDto implements SearchDto{
	
	private Integer pageNum;
	
	private Integer pageSize;
	
	@EsPage
	private EsPageParam page = new EsPageParam();
	
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		this.page.setPageNum(pageNum);
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		this.page.setPageSize(pageSize);
	}
	public EsPageParam getPage() {
		return page;
	}
	public void setPage(EsPageParam page) {
		this.page = page;
	}
}
