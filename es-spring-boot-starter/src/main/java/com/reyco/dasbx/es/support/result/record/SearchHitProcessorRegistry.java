package com.reyco.dasbx.es.support.result.record;

import java.util.List;

import com.reyco.dasbx.es.core.result.SearchHitResult;

public class SearchHitProcessorRegistry {
	
	private List<SearchHitResultProcessor> searchHitResultProcessors;
	
	public SearchHitProcessorRegistry(List<SearchHitResultProcessor> searchHitResultProcessors) {
		super();
		this.searchHitResultProcessors = searchHitResultProcessors;
	}

	public <T> void process(T entity, SearchHitResult<T> hitResult) {
		searchHitResultProcessors.stream().forEach(searchHitProcessor->searchHitProcessor.process(entity, hitResult));
	}
	
}
