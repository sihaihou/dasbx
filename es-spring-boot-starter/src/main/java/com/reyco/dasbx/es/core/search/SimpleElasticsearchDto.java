package com.reyco.dasbx.es.core.search;

public class SimpleElasticsearchDto implements ElasticsearchDto {
	private String keyword;
	private Integer pageNum=1;
	private Integer pageSize=10;
	@Override
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@Override
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	@Override
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
