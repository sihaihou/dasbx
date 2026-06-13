package com.reyco.dasbx.es.test.template;

import com.reyco.dasbx.es.support.annotation.page.EsPage;
import com.reyco.dasbx.es.support.annotation.page.EsPageParam;
import com.reyco.dasbx.es.support.template.SearchDto;

public class BaseDto implements SearchDto {
	
	@EsPage
	private EsPageParam esPageParam;
	
	public EsPageParam getPageParam() {
		return esPageParam;
	}

	public void setPageParam(EsPageParam esPageParam) {
		this.esPageParam = esPageParam;
	}
	
}
