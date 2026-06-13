package com.reyco.dasbx.es.core.pipeline.result;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.result.strategy.PageStrategyRegistry;

public class PagePipeline implements SearchPipeline {

	private PageStrategyRegistry registry;

	public PagePipeline(PageStrategyRegistry registry) {
		super();
		this.registry = registry;
	}

	@Override
	public void execute(SearchContext context) {
		try {
			SearchQuery query = context.getQuery();
			if (query == null) {
				return;
			}
			registry.apply(query.getPage(), context);
		}catch (Exception e) {
			throw new SearchBuildException("Page build error!",e);
		}
	}
	
	@Override
	public int getOrder() {
		return 70;
	}
}