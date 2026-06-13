package com.reyco.dasbx.es.support.result.record;

import com.reyco.dasbx.es.core.result.SearchHitResult;

public interface SearchHitResultProcessor {
	
	<T> void process(T entity, SearchHitResult<T> result);
	
}
