package com.reyco.dasbx.es.core.feature.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.reyco.dasbx.es.core.query.SearchContext;

public class SearchInterceptorPipeline {

	private static final Logger log = LoggerFactory.getLogger(SearchInterceptorPipeline.class);

	private final List<SearchRequestCustomizer<?>> customizers;

	private final List<SearchExecutionInterceptor> executionInterceptors;

	public SearchInterceptorPipeline(List<SearchRequestCustomizer<?>> customizers,
			List<SearchExecutionInterceptor> executionInterceptors) {
		this.customizers = customizers == null ? new ArrayList<>() : new ArrayList<>(customizers);
		this.executionInterceptors = executionInterceptors == null ? new ArrayList<>()
				: new ArrayList<>(executionInterceptors);

		AnnotationAwareOrderComparator.sort(this.customizers);
		AnnotationAwareOrderComparator.sort(this.executionInterceptors);
	}

	public void before(SearchContext context, SearchOperationType operation, ActionRequest request) {

		customize(context, request);

		for (SearchExecutionInterceptor interceptor : executionInterceptors) {
			interceptor.before(context, operation, request);
		}
	}

	public void onSuccess(SearchContext context, SearchOperationType operation, ActionRequest request,
			SearchResponse response) {

		for (int i = executionInterceptors.size() - 1; i >= 0; i--) {
			SearchExecutionInterceptor interceptor = executionInterceptors.get(i);

			try {
				interceptor.onSuccess(context, operation, request, response);
			} catch (Exception e) {
				log.warn("Search success interceptor failed: {}", interceptor.getClass().getName(), e);
			}
		}
	}

	public void onFailure(SearchContext context, SearchOperationType operation, ActionRequest request,
			Exception exception) {

		for (int i = executionInterceptors.size() - 1; i >= 0; i--) {
			SearchExecutionInterceptor interceptor = executionInterceptors.get(i);

			try {
				interceptor.onFailure(context, operation, request, exception);
			} catch (Exception e) {
				log.warn("Search failure interceptor failed: {}", interceptor.getClass().getName(), e);
			}
		}
	}

	private <R extends ActionRequest> void customize(SearchContext context, R request) {

		for (SearchRequestCustomizer<?> customizer : customizers) {
			if (customizer.support().isInstance(request)) {
				applyMatched(customizer, context, request);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <R extends ActionRequest> void applyMatched(SearchRequestCustomizer<?> customizer, SearchContext context,
			R request) {

		SearchRequestCustomizer<R> matched = (SearchRequestCustomizer<R>) customizer;

		matched.apply(context, request);
	}
}
