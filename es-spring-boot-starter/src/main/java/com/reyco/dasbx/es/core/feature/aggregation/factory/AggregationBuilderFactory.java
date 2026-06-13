package com.reyco.dasbx.es.core.feature.aggregation.factory;

import org.elasticsearch.search.aggregations.AggregationBuilder;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.handler.AggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.handler.AggregationHandlerRegistry;
import com.reyco.dasbx.es.core.query.SearchContext;

public class AggregationBuilderFactory {
	
	private AggregationHandlerRegistry registry;

	public AggregationBuilderFactory(AggregationHandlerRegistry registry) {
		super();
		this.registry = registry;
	}

	public AggregationBuilder build(AggregationDefinition agg,SearchContext context) {
		AggregationHandler handler = registry.get(agg.getType());
		if (handler == null) {
			throw new RuntimeException("unsupported aggregation: " + agg.getType());
		}

		AggregationBuilder builder = handler.build(agg);
		/*
		 * 子聚合递归
		 */
		if (agg.getChildren() != null) {
			for (AggregationDefinition child : agg.getChildren()) {
				builder.subAggregation(build(child,context));
			}
		}

		return builder;
	}
}
