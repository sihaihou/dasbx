package com.reyco.dasbx.es.core.query.builder.reactive.executor;

import org.elasticsearch.action.search.SearchResponse;

import com.reyco.dasbx.es.core.query.SearchContext;

import reactor.core.publisher.Mono;

public interface ReactiveSearchExecutor {

	Mono<SearchResponse> execute(SearchContext context);
	
}