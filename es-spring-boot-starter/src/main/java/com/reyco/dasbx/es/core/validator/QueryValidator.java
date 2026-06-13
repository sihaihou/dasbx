package com.reyco.dasbx.es.core.validator;

import org.springframework.core.Ordered;

import com.reyco.dasbx.es.core.query.SearchContext;

public interface QueryValidator extends Ordered{

	void validate(SearchContext context);

	@Override
	default int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
	
}
