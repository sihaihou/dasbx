package com.reyco.dasbx.es.core.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.reyco.dasbx.commons.utils.Page;
import com.reyco.dasbx.es.core.model.Aggregation;

public interface ElasticsearchVO<T> {
	
	Map<String, List<Aggregation>> getAggregations() throws Exception;
	
	Page<T> getPage() throws IOException;
}
