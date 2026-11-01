package com.reyco.dasbx.es.core.pipeline.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.feature.interceptor.SearchOperationType;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.validator.QueryValidator;

public class QueryValidatorPipeline implements SearchPipeline {

	private final List<QueryValidator> validators;

	public QueryValidatorPipeline(List<QueryValidator> validators) {
		List<QueryValidator> sorted = new ArrayList<>(validators);
		AnnotationAwareOrderComparator.sort(sorted);

		this.validators = sorted;
	}

	@Override
	public boolean supports(SearchOperationType operation) {
		return operation == SearchOperationType.SEARCH || operation == SearchOperationType.SCROLL;
	}

	@Override
	public void execute(SearchContext context) {
		SearchOperationType operation = context.getOperation();

		if (operation == null) {
			throw new SearchBuildException("Search operation type must not be null");
		}

		try {
			for (QueryValidator validator : validators) {
				if (!validator.supports(operation)) {
					continue;
				}
				validator.validate(context);
			}
		} catch (SearchBuildException e) {
			throw e;
		} catch (Exception e) {
			throw new SearchBuildException("QueryValidator build error!", e);
		}
	}

	@Override
	public int getOrder() {
		return 20;
	}
}
