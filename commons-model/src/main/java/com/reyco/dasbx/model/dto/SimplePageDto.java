package com.reyco.dasbx.model.dto;

import com.reyco.dasbx.model.constants.Constants;

public class SimplePageDto implements PageDto{
	
	private Integer pageSize = Constants.DEFAULT_PAGE_SIZE;
	private Integer pageNum = 1;
	@Override
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
}
