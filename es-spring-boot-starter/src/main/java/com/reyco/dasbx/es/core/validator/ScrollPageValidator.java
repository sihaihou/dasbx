package com.reyco.dasbx.es.core.validator;

import org.elasticsearch.common.unit.TimeValue;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.feature.interceptor.SearchOperationType;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.PageDefinition;
import com.reyco.dasbx.es.core.result.page.ScrollPageDefinition;

public class ScrollPageValidator implements QueryValidator {

	@Override
	public boolean supports(SearchOperationType operation) {
		return operation == SearchOperationType.SEARCH
				|| operation == SearchOperationType.SCROLL;
	}

	@Override
	public void validate(SearchContext context) {
		PageDefinition definition =
				context.getQuery().getPage();

		if (!(definition instanceof ScrollPageDefinition)) {
			if (context.getOperation()
					== SearchOperationType.SCROLL) {
				throw new SearchBuildException(
						"page must be ScrollPageDefinition");
			}
			return;
		}

		ScrollPageDefinition page =
				(ScrollPageDefinition) definition;

		TimeValue keepAlive = page.getKeepAlive();

		if (keepAlive == null || keepAlive.millis() <= 0) {
			throw new SearchBuildException(
					"scroll keepAlive must be greater than zero");
		}

		if (context.getOperation()
				== SearchOperationType.SEARCH) {
			Integer pageSize = page.getPageSize();

			if (pageSize == null || pageSize <= 0) {
				throw new SearchBuildException(
						"scroll pageSize must be greater than zero");
			}

			if (page.getScrollId() != null) {
				throw new SearchBuildException(
						"initial scroll page must not have scrollId");
			}
			return;
		}

		String scrollId = page.getScrollId();

		if (scrollId == null || scrollId.trim().isEmpty()) {
			throw new SearchBuildException(
					"scrollId must not be empty");
		}
	}

	@Override
	public int getOrder() {
		return 10;
	}
}
