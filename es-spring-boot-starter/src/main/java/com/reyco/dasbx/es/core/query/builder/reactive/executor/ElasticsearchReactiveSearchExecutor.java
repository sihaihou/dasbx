package com.reyco.dasbx.es.core.query.builder.reactive.executor;

import org.elasticsearch.action.search.SearchResponse;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.builder.executor.SearchExecutor;

import reactor.core.publisher.Mono;

public class ElasticsearchReactiveSearchExecutor implements ReactiveSearchExecutor {

	private SearchExecutor executor;

	@Override
	public Mono<SearchResponse> execute(SearchContext context) {
		return Mono.fromFuture(executor.executeAsync(context));
	}
	
}
