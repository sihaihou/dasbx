package com.reyco.dasbx.es.core.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.reyco.dasbx.es.core.explain.SearchExplain;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.profile.PipelineProfile;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.builder.executor.SearchExecutor;

public class SearchEngine {

	private List<SearchPipeline> pipelines;

	private SearchExecutor executor;

	public SearchEngine(List<SearchPipeline> pipelines, SearchExecutor executor) {
		List<SearchPipeline> sortedPipelines = new ArrayList<>(pipelines);
		AnnotationAwareOrderComparator.sort(sortedPipelines);

		this.pipelines = sortedPipelines;
		this.executor = executor;
	}

	/**
	 * 同步执行
	 */
	public SearchResponse execute(SearchContext context) {
		processPipeline(context);
		return executor.execute(context);
	}

	/**
	 * 异步执行
	 */
	public CompletableFuture<SearchResponse> executeAsync(SearchContext context) {
		processPipeline(context);
		return executor.executeAsync(context);
	}

	/**
	 * pipeline
	 * 
	 * @throws Exception
	 */
	private void processPipeline(SearchContext context) {
		
		recordOriginalQuery(context);
		
		long totalStart = System.nanoTime();
		
		for (SearchPipeline pipeline : pipelines) {

			long start = System.nanoTime();

			pipeline.execute(context);

			long end = System.nanoTime();
			recordPipeline(context, pipeline, start, end);
		}
		
		long totalEnd = System.nanoTime();

		SearchProfile profile = context.getProfile();
		if (profile != null) {
			profile.setTotalTook((totalEnd - totalStart) / 1_000_000);
		}
		
		recordFinalDsl(context);
	}

	private void recordPipeline(SearchContext context, SearchPipeline pipeline, long start, long end) {
		SearchProfile profile = context.getProfile();
		if (profile == null) {
			return;
		}

		PipelineProfile p = new PipelineProfile();
		p.setName(pipeline.getClass().getSimpleName());
		p.setTook((end - start) / 1_000_000);

		profile.addPipeline(p);
	}
	public void recordOriginalQuery(SearchContext context) {
	    SearchQuery query = context.getQuery();
	    if (query != null) {
	    	 context.getExplain().setOriginalQuery(
	                 JSON.toJSONString(query,SerializerFeature.PrettyFormat, SerializerFeature.SortField));
	    }
	}
	private void recordFinalDsl(SearchContext context) {
	    SearchExplain explain = context.getExplain();
	    if (explain == null) {
	        return;
	    }

	    if (context.getSourceBuilder() != null) {
	        explain.setFinalDsl(prettyJson(context.getSourceBuilder().toString()));
	    }
	}
	public static String prettyJson(String json) {
	    try {
	        Object obj = JSON.parse(json);
	        return JSON.toJSONString(
	                obj,
	                SerializerFeature.PrettyFormat,
	                SerializerFeature.WriteMapNullValue);
	    } catch (Exception e) {
	        return json;
	    }
	}
}
