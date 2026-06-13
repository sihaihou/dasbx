package com.reyco.dasbx.es.core.result.page;

public class Pages {
	
	public static final Integer DEFAULT_PAGE_NUM = 1;
	
	public static final Integer DEFAULT_PAGE_SIZE = 10;
	
	/**
	 * 普通分页
	 */
	public static OffsetPageDefinition offset(){
		return new OffsetPageDefinition();
	}
	public static OffsetPageDefinition offset(int pageNum,int pageSize){
		return new OffsetPageDefinition()
				.setPageNum(pageNum)
				.setPageSize(pageSize);
	}

	/**
	 * search after
	 */
	public static SearchAfterPageDefinition searchAfter(int pageSize){
		return new SearchAfterPageDefinition()
				.setPageSize(pageSize);
	}
	public static SearchAfterPageDefinition searchAfter(Object[] values,int pageSize){
		return new SearchAfterPageDefinition()
				.setSearchAfter(values)
				.setPageSize(pageSize);
	}
	/**
	 * scroll
	 */
	public static ScrollPageDefinition scroll(String scrollId,int pageSize){
		return new ScrollPageDefinition()
				.setScrollId(scrollId)
				.setPageSize(pageSize);
	}
}
