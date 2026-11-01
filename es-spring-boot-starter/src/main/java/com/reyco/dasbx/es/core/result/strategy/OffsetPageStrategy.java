package com.reyco.dasbx.es.core.result.strategy;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.OffsetPageDefinition;

public class OffsetPageStrategy implements PageStrategy<OffsetPageDefinition> {

	@Override
	public Class<OffsetPageDefinition> support() {
		return OffsetPageDefinition.class;
	}

	@Override
	public void apply(OffsetPageDefinition page, SearchContext context) {
		int pageNum = page.getPageNum();
		int pageSize = page.getPageSize();
		int from = (pageNum - 1) * pageSize;

		context.getSourceBuilder()
				.from(from)
				.size(pageSize);
	}

}
