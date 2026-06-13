package com.reyco.dasbx.es.core.query.builder.executor;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import com.reyco.dasbx.es.core.exception.SearchExecuteException;
import com.reyco.dasbx.es.core.feature.interceptor.SearchRequestInterceptorChain;
import com.reyco.dasbx.es.core.log.DslLogger;
import com.reyco.dasbx.es.core.profile.PipelineProfile;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.SearchContext;

public class ElasticsearchSearchExecutor implements SearchExecutor {

	private RestHighLevelClient client;

	private SearchRequestInterceptorChain interceptorChain;

	private DslLogger dslLogger;

	public ElasticsearchSearchExecutor(RestHighLevelClient client, SearchRequestInterceptorChain interceptorChain,
			DslLogger dslLogger) {
		super();
		this.client = client;
		this.interceptorChain = interceptorChain;
		this.dslLogger = dslLogger;
	}

	@Override
	public SearchResponse execute(SearchContext context) {
		long start = System.nanoTime();
		try {
			SearchRequest request = context.getRequest();
			//
			interceptorChain.apply(context, request);

			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			context.setResponse(response);
			dslLogger.log(context,System.nanoTime()-start);

			long end = System.nanoTime();
			SearchProfile profile = context.getProfile();
			if (profile != null) {
				PipelineProfile p = new PipelineProfile();
				p.setName("SearchExecutor");
				p.setTook((end - start) / 1_000_000);
				profile.addPipeline(p);
			}
			return response;
		} catch (IOException e) {
			dslLogger.error(context,System.nanoTime()-start,e);
			throw new SearchExecuteException("executor error,", e);
		}

	}

	@Override
	public CompletableFuture<SearchResponse> executeAsync(SearchContext context) {
		long start = System.nanoTime();
		CompletableFuture<SearchResponse> future = new CompletableFuture<>();
		client.searchAsync(context.getRequest(), RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
			@Override
			public void onResponse(SearchResponse response) {
				context.setResponse(response);
				dslLogger.log(context,System.nanoTime()-start);
				future.complete(response);
			}

			@Override
			public void onFailure(Exception e) {
				dslLogger.error(context,System.nanoTime()-start, e);
				future.completeExceptionally(new SearchExecuteException("executeAsync error", e));
			}
		});
		return future;
	}

}
