package com.reyco.dasbx.es.core.result.strategy;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.SearchAfterPageDefinition;

public class SearchAfterPageStrategy implements PageStrategy<SearchAfterPageDefinition> {

	@Override
	public Class<SearchAfterPageDefinition> support() {
		return SearchAfterPageDefinition.class;
	}

	@Override
	public void apply(SearchAfterPageDefinition page, SearchContext context) {
		context.getSourceBuilder().size(page.getPageSize());
		if (page.getSearchAfter() != null) {
			context.getSourceBuilder().searchAfter(page.getSearchAfter());
		}
	}
}
