package com.reyco.dasbx.es.core.validator;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.result.page.OffsetPageDefinition;

public class PageValidator implements QueryValidator {

	private static final int MAX_PAGE_SIZE = 1000;

	private static final long MAX_RESULT_WINDOW = 10000L;

	@Override
	public void validate(SearchContext context) {
		SearchQuery query = context.getQuery();

		if (query == null || !(query.getPage() instanceof OffsetPageDefinition)) {
			return;
		}

		OffsetPageDefinition page = (OffsetPageDefinition) query.getPage();

		Integer pageNum = page.getPageNum();
		Integer pageSize = page.getPageSize();

		if (pageNum == null || pageNum <= 0) {
			throw new SearchBuildException("Offset pageNum must be greater than zero");
		}

		if (pageSize == null || pageSize <= 0) {
			throw new SearchBuildException("Offset pageSize must be greater than zero");
		}

		if (pageSize > MAX_PAGE_SIZE) {
			throw new SearchBuildException("Offset pageSize must not exceed " + MAX_PAGE_SIZE);
		}

		long resultWindow = (long) pageSize * pageNum;

		if (resultWindow > MAX_RESULT_WINDOW) {
			throw new SearchBuildException("deep pagination please use search_after");
		}
	}

	@Override
	public int getOrder() {
		return 10;
	}
}
