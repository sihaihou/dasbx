package com.reyco.dasbx.es.core.validator;

import java.util.List;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.result.page.PageDefinition;
import com.reyco.dasbx.es.core.result.page.SearchAfterPageDefinition;

public class SearchAfterValidator implements QueryValidator{

	@Override
	public void validate(SearchContext context) {
		SearchQuery query = context.getQuery();
		PageDefinition page = query.getPage();
		
		if (page == null || (page!=null && !(page instanceof SearchAfterPageDefinition))) {
			return;
		}
		List<SortDefinition> sorts = query.getSorts();

		if (sorts==null || sorts.isEmpty()) {
			throw new SearchBuildException("SearchAfter Page must have sorting!");
		}
	}
	
	@Override
	public int getOrder() {
		return 11;
	}
	
}
