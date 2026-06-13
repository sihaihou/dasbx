package com.reyco.dasbx.es.core.feature.interceptor;

import org.elasticsearch.action.search.SearchRequest;

import com.reyco.dasbx.es.core.query.SearchContext;

public class AliasInterceptor implements SearchRequestInterceptor {

	@Override
	public void apply(SearchContext context, SearchRequest request) {
		request.indices(context.getResolvedIndices().toArray(new String[0]));
		request.source(context.getSourceBuilder());
	}
	
	@Override
	public int getOrder() {
		return -1;
	}
}
