package com.reyco.dasbx.es.core.query.builder.executor;

import java.util.concurrent.CompletableFuture;

import org.elasticsearch.action.search.SearchResponse;

import com.reyco.dasbx.es.core.query.SearchContext;

public interface SearchExecutor {

	SearchResponse execute(SearchContext context);
	
	CompletableFuture<SearchResponse> executeAsync(SearchContext context);

	SearchResponse scroll(SearchContext context);

	CompletableFuture<SearchResponse> scrollAsync(SearchContext context);

	boolean clearScroll(String scrollId);
}
