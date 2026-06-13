package com.reyco.dasbx.es.core.feature.aggregation.handler;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;

public interface AggregationHandler{
	/**
	 * 聚合类型
	 */
	AggregationType type();
	/**
	 * 构建DSL
	 */
	AggregationBuilder build(AggregationDefinition agg);

	/**
	 * 聚合结果解析
	 */
	AggregationNode parse(Aggregation aggregation);

}
