package com.reyco.dasbx.es.core.result.strategy;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.ScrollPageDefinition;

public class ScrollPageStrategy implements PageStrategy<ScrollPageDefinition> {

	@Override
	public Class<ScrollPageDefinition> support() {
		return ScrollPageDefinition.class;
	}

	@Override
	public void apply(ScrollPageDefinition page, SearchContext context) {
		context.getSourceBuilder().size(page.getPageSize());
		context.getRequest().scroll(page.getKeepAlive());
	}
}
