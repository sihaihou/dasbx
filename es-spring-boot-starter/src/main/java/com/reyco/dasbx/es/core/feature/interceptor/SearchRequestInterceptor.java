package com.reyco.dasbx.es.core.feature.interceptor;

import org.elasticsearch.action.search.SearchRequest;
import org.springframework.core.Ordered;

import com.reyco.dasbx.es.core.query.SearchContext;

public interface SearchRequestInterceptor extends Ordered {
	
	default void before(SearchContext context, SearchRequest request) {}
	
	void apply(SearchContext context, SearchRequest request);
	
	default void after(SearchContext context, SearchRequest request) {}
	
	@Override
	default int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
