package com.reyco.dasbx.es.core.pipeline.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
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
	public void execute(SearchContext context) {
		try {
			for (QueryValidator validator : validators) {
				validator.validate(context);
			}
		}catch (Exception e) {
			throw new SearchBuildException("QueryValidator build error!",e);
		}
	}

	@Override
	public int getOrder() {
		return 20;
	}
}
