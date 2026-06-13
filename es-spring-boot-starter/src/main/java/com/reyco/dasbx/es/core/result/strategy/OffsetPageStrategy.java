package com.reyco.dasbx.es.core.result.strategy;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.OffsetPageDefinition;

public class OffsetPageStrategy implements PageStrategy<OffsetPageDefinition> {

	private static final int MAX_FROM = 10000;

	@Override
	public Class<OffsetPageDefinition> support() {
		return OffsetPageDefinition.class;
	}

	@Override
	public void apply(OffsetPageDefinition page, SearchContext context) {
		int from = (page.getPageNum() - 1) * page.getPageSize();
		if (from > MAX_FROM) {
			throw new SearchBuildException("deep pagination please use search_after");
		}
		context.getSourceBuilder().from(from).size(page.getPageSize());
	}
	
}
