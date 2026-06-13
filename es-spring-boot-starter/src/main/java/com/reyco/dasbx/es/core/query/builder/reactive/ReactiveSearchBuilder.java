package com.reyco.dasbx.es.core.query.builder.reactive;

import com.reyco.dasbx.es.core.result.SearchHitResult;
import com.reyco.dasbx.es.core.result.SearchResult;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveSearchBuilder<T> {
	
	/**
	 * 普通reactive查询
	 * @return
	 */
	Mono<SearchResult<T>> mono();
	/**
	 * 流式hit
	 * @return
	 */
	Flux<SearchHitResult<T>> flux();
	
	/**
	 * 
	 * @return
	 */
	Flux<SearchHitResult<T>> searchAfterFlux();
	
}