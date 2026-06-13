package com.reyco.dasbx.es.core.feature.aggregation.handler;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Max;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;

public class MaxAggregationHandler implements AggregationHandler {

	@Override
	public AggregationType type() {
		return AggregationType.MAX;
	}

	@Override
	public AggregationBuilder build(AggregationDefinition agg) {
		return AggregationBuilders.max(agg.getName()).field(agg.getField());
	}

	@Override
	public AggregationNode parse(Aggregation aggregation) {
		Max max = (Max) aggregation;
		
		AggregationNode node = new AggregationNode();
		node.setName(max.getName());
		node.setType(AggregationType.MAX);
		node.setValue(max.getValue());
		return node;
	}
}
