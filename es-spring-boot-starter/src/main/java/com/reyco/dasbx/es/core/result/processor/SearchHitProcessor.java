package com.reyco.dasbx.es.core.result.processor;

import org.elasticsearch.search.SearchHit;
import org.springframework.core.Ordered;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.SearchHitResult;

public interface SearchHitProcessor extends Ordered{

	<T> void process(SearchContext context, SearchHit hit, T entity, SearchHitResult<T> result);

}
