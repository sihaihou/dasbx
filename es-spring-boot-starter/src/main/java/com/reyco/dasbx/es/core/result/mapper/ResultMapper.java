package com.reyco.dasbx.es.core.result.mapper;

import org.elasticsearch.action.search.SearchResponse;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.SearchResult;

public interface ResultMapper {

	<T> SearchResult<T> map(SearchContext context, SearchResponse response, Class<T> clazz) throws Exception;
	
}
