package com.reyco.dasbx.es.core.result.parser;

import org.elasticsearch.search.SearchHit;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.SearchHitResult;

public interface SearchHitParser {
	
	<T> SearchHitResult<T> parse(SearchHit hit, Class<T> clazz, SearchContext context);

}
