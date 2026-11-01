package com.reyco.dasbx.es.core.feature.interceptor;

import org.elasticsearch.action.ActionRequest;
import org.springframework.core.Ordered;

import com.reyco.dasbx.es.core.query.SearchContext;

public interface SearchRequestCustomizer<R extends ActionRequest> extends Ordered {

	Class<R> support();

	void apply(SearchContext context, R request);

	@Override
	default int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
