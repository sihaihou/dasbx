package com.reyco.dasbx.user.core.model.dto.sys;

import com.reyco.dasbx.es.dto.SearchPageDto;
import com.reyco.dasbx.es.support.annotation.EsIndex;
import com.reyco.dasbx.es.support.annotation.highlight.EsHighlightField;
import com.reyco.dasbx.es.support.annotation.query.EsQuery;
import com.reyco.dasbx.es.support.annotation.query.EsQueryType;

@EsIndex("sys_role")
public class SysRoleSearchDto extends SearchPageDto{
	
	@EsQuery(type=EsQueryType.MATCH,field= "all")
	private String keyword;
	
	@EsHighlightField
	private String name;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
