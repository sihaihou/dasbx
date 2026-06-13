package com.reyco.dasbx.es.core.validator;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.result.page.OffsetPageDefinition;
import com.reyco.dasbx.es.core.result.page.PageDefinition;

public class PageValidator implements QueryValidator {

	@Override
	public void validate(SearchContext context) {
		SearchQuery query = context.getQuery();
		PageDefinition page = query.getPage();
		if (page == null || (page!=null && !(page instanceof OffsetPageDefinition))) {
			return;
		}
		OffsetPageDefinition offsetPageDefinition = (OffsetPageDefinition)page;
		Integer pageSize = offsetPageDefinition.getPageSize();
		Integer pageNum = offsetPageDefinition.getPageNum();
		if (pageSize <= 0 
				|| pageNum<=0 
				|| pageSize>1000
				|| pageNum>1000
				|| pageSize*pageNum>10000) {
			throw new SearchBuildException("Offset paging parameter setting error");
		}
	}
	
	@Override
	public int getOrder() {
		return 10;
	}
}
