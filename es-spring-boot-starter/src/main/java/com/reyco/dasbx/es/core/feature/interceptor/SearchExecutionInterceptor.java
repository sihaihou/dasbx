package com.reyco.dasbx.es.core.feature.interceptor;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.core.Ordered;

import com.reyco.dasbx.es.core.query.SearchContext;

public interface SearchExecutionInterceptor extends Ordered {

	default void before(
			SearchContext context,
			SearchOperationType operation,
			ActionRequest request) {
	}

	default void onSuccess(
			SearchContext context,
			SearchOperationType operation,
			ActionRequest request,
			SearchResponse response) {
	}

	default void onFailure(
			SearchContext context,
			SearchOperationType operation,
			ActionRequest request,
			Exception exception) {
	}

	@Override
	default int getOrder() {
		return LOWEST_PRECEDENCE;
	}
}
