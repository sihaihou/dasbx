package com.reyco.dasbx.es.core.query.builder.executor;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.exception.SearchException;
import com.reyco.dasbx.es.core.exception.SearchExecuteException;
import com.reyco.dasbx.es.core.feature.interceptor.SearchInterceptorPipeline;
import com.reyco.dasbx.es.core.feature.interceptor.SearchOperationType;
import com.reyco.dasbx.es.core.log.DslLogger;
import com.reyco.dasbx.es.core.profile.PipelineProfile;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.PageDefinition;
import com.reyco.dasbx.es.core.result.page.ScrollPageDefinition;

public class ElasticsearchSearchExecutor implements SearchExecutor {

	private final RestHighLevelClient client;

	private final SearchInterceptorPipeline interceptorPipeline;

	private final DslLogger dslLogger;

	public ElasticsearchSearchExecutor(RestHighLevelClient client, SearchInterceptorPipeline interceptorPipeline,
			DslLogger dslLogger) {
		this.client = client;
		this.interceptorPipeline = interceptorPipeline;
		this.dslLogger = dslLogger;
	}

	@Override
	public SearchResponse execute(SearchContext context) {
		long start = System.nanoTime();
		SearchRequest request = context.getRequest();

		try {

			interceptorPipeline.before(context, SearchOperationType.SEARCH, request);

			SearchResponse response = client.search(request, RequestOptions.DEFAULT);

			context.setResponse(response);
			dslLogger.log(context, System.nanoTime() - start);

			recordProfile(context, start);

			interceptorPipeline.onSuccess(context, SearchOperationType.SEARCH, request, response);

			return response;
		} catch (Exception e) {
			dslLogger.error(context, System.nanoTime() - start, e);

			interceptorPipeline.onFailure(context, SearchOperationType.SEARCH, request, e);

			if (e instanceof SearchException) {
				throw (SearchException) e;
			}

			throw new SearchExecuteException("executor error", e);
		}
	}

	@Override
	public CompletableFuture<SearchResponse> executeAsync(SearchContext context) {
		long start = System.nanoTime();
		SearchRequest request = context.getRequest();
		CompletableFuture<SearchResponse> future = new CompletableFuture<>();

		try {
			interceptorPipeline.before(context, SearchOperationType.SEARCH, request);
		} catch (Exception e) {
			dslLogger.error(context, System.nanoTime() - start, e);

			interceptorPipeline.onFailure(context, SearchOperationType.SEARCH, request, e);

			future.completeExceptionally(wrapException("interceptor error", e));
			return future;
		}

		client.searchAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
			@Override
			public void onResponse(SearchResponse response) {
				context.setResponse(response);
				dslLogger.log(context, System.nanoTime() - start);

				recordProfile(context, start);

				interceptorPipeline.onSuccess(context, SearchOperationType.SEARCH, request, response);

				future.complete(response);
			}

			@Override
			public void onFailure(Exception e) {
				dslLogger.error(context, System.nanoTime() - start, e);

				interceptorPipeline.onFailure(context, SearchOperationType.SEARCH, request, e);

				future.completeExceptionally(wrapException("executeAsync error", e));
			}
		});
		return future;
	}

	@Override
	public SearchResponse scroll(SearchContext context) {
		long start = System.nanoTime();
		SearchScrollRequest request = createScrollRequest(context);

		try {
			interceptorPipeline.before(context, SearchOperationType.SCROLL, request);

			SearchResponse response = client.scroll(request, RequestOptions.DEFAULT);

			context.setResponse(response);
			dslLogger.log(context, System.nanoTime() - start);

			interceptorPipeline.onSuccess(context, SearchOperationType.SCROLL, request, response);

			return response;
		} catch (Exception e) {
			dslLogger.error(context, System.nanoTime() - start, e);

			interceptorPipeline.onFailure(context, SearchOperationType.SCROLL, request, e);

			if (e instanceof SearchException) {
				throw (SearchException) e;
			}

			throw new SearchExecuteException("scroll error", e);
		}
	}

	@Override
	public CompletableFuture<SearchResponse> scrollAsync(SearchContext context) {
		long start = System.nanoTime();
		CompletableFuture<SearchResponse> future = new CompletableFuture<>();
		SearchScrollRequest request;

		try {
			request = createScrollRequest(context);
		} catch (RuntimeException e) {
			dslLogger.error(context, System.nanoTime() - start, e);
			future.completeExceptionally(e);
			return future;
		}

		try {
			interceptorPipeline.before(context, SearchOperationType.SCROLL, request);
		} catch (Exception e) {
			dslLogger.error(context, System.nanoTime() - start, e);

			interceptorPipeline.onFailure(context, SearchOperationType.SCROLL, request, e);

			future.completeExceptionally(wrapException("scroll interceptor error", e));
			return future;
		}

		client.scrollAsync(request, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
			@Override
			public void onResponse(SearchResponse response) {
				context.setResponse(response);
				dslLogger.log(context, System.nanoTime() - start);

				interceptorPipeline.onSuccess(context, SearchOperationType.SCROLL, request, response);

				future.complete(response);
			}

			@Override
			public void onFailure(Exception e) {
				dslLogger.error(context, System.nanoTime() - start, e);

				interceptorPipeline.onFailure(context, SearchOperationType.SCROLL, request, e);

				future.completeExceptionally(wrapException("scrollAsync error", e));
			}
		});
		return future;
	}

	@Override
	public boolean clearScroll(String scrollId) {
		if (scrollId == null || scrollId.trim().isEmpty()) {
			throw new SearchBuildException("scrollId must not be empty");
		}

		ClearScrollRequest request = new ClearScrollRequest();
		request.addScrollId(scrollId);

		try {
			ClearScrollResponse response = client.clearScroll(request, RequestOptions.DEFAULT);
			return response.isSucceeded();
		} catch (IOException e) {
			throw new SearchExecuteException("clearScroll error", e);
		}
	}

	private void recordProfile(SearchContext context, long start) {
		SearchProfile profile = context.getProfile();

		if (profile == null) {
			return;
		}

		PipelineProfile pipelineProfile = new PipelineProfile();
		pipelineProfile.setName("SearchExecutor");
		pipelineProfile.setTook((System.nanoTime() - start) / 1_000_000);
		profile.addPipeline(pipelineProfile);
	}

	private SearchException wrapException(String message, Exception exception) {
		if (exception instanceof SearchException) {
			return (SearchException) exception;
		}

		return new SearchExecuteException(message, exception);
	}

	private SearchScrollRequest createScrollRequest(SearchContext context) {
		PageDefinition page = context.getQuery().getPage();

		if (!(page instanceof ScrollPageDefinition)) {
			throw new SearchBuildException("page must be ScrollPageDefinition");
		}

		ScrollPageDefinition scrollPage = (ScrollPageDefinition) page;
		String scrollId = scrollPage.getScrollId();

		if (scrollId == null || scrollId.trim().isEmpty()) {
			throw new SearchBuildException("scrollId must not be empty");
		}

		return new SearchScrollRequest(scrollId).scroll(scrollPage.getKeepAlive());
	}

}
