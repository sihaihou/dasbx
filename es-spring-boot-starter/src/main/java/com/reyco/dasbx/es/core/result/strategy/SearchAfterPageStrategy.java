package com.reyco.dasbx.es.core.result.strategy;

import java.util.List;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.result.page.SearchAfterPageDefinition;

public class SearchAfterPageStrategy implements PageStrategy<SearchAfterPageDefinition> {

	@Override
	public Class<SearchAfterPageDefinition> support() {
		return SearchAfterPageDefinition.class;
	}

	@Override
	public void apply(SearchAfterPageDefinition page, SearchContext context) {
		context.getSourceBuilder().size(page.getPageSize());

		Object[] searchAfter = page.getSearchAfter();
		if (searchAfter == null) {
			return;
		}

		List<SortDefinition> sorts = context.getQuery().getSorts();
		int sortCount = sorts == null ? 0 : sorts.size();

		if (searchAfter.length != sortCount) {
			throw new SearchBuildException(
					"search_after value count must match sort count");
		}

		context.getSourceBuilder().searchAfter(searchAfter);
	}
}
