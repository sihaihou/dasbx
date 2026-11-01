package com.reyco.dasbx.es.core.validator;

import java.util.List;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.result.page.SearchAfterPageDefinition;

public class SearchAfterValidator implements QueryValidator {

	@Override
	public void validate(SearchContext context) {
		SearchQuery query = context.getQuery();

		if (query == null || !(query.getPage() instanceof SearchAfterPageDefinition)) {
			return;
		}

		SearchAfterPageDefinition page = (SearchAfterPageDefinition) query.getPage();

		Integer pageSize = page.getPageSize();

		if (pageSize == null || pageSize <= 0) {
			throw new SearchBuildException("search_after pageSize must be greater than zero");
		}

		List<SortDefinition> sorts = query.getSorts();

		if (sorts == null || sorts.isEmpty()) {
			throw new SearchBuildException("SearchAfter Page must have sorting!");
		}

		Object[] searchAfter = page.getSearchAfter();

		if (searchAfter != null && searchAfter.length == 0) {
			throw new SearchBuildException("search_after values must not be empty");
		}
	}

	@Override
	public int getOrder() {
		return 11;
	}

}
