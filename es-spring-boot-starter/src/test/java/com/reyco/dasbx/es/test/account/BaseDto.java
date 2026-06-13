package com.reyco.dasbx.es.test.account;

import com.reyco.dasbx.es.support.annotation.query.EsQuery;
import com.reyco.dasbx.es.support.annotation.query.EsQueryType;

public class BaseDto {
	
	@EsQuery(type=EsQueryType.MULTI_MATCH,multiFields= {"nickname","username","phone","email"})
	private String keyword;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
