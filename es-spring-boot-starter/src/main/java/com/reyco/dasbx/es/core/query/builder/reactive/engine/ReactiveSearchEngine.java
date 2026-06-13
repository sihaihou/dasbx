package com.reyco.dasbx.es.core.query.builder.reactive.engine;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;

import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.builder.reactive.executor.ReactiveSearchExecutor;

import reactor.core.publisher.Mono;

public class ReactiveSearchEngine {
	
	private List<SearchPipeline> pipelines;

	private ReactiveSearchExecutor executor;

	public ReactiveSearchEngine(List<SearchPipeline> pipelines, ReactiveSearchExecutor executor) {
		super();
		this.pipelines = pipelines;
		this.executor = executor;
	}

	public Mono<SearchResponse> execute(SearchContext context) {
		for (SearchPipeline pipeline : pipelines) {
			pipeline.execute(context);
		}
		return executor.execute(context);
	}
}
