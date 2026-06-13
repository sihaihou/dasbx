package com.reyco.dasbx.es.core.feature.aggregation.handler;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Min;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;

public class MinAggregationHandler implements AggregationHandler {

	@Override
	public AggregationType type() {
		return AggregationType.MIN;
	}

	@Override
	public AggregationBuilder build(AggregationDefinition agg) {
		return AggregationBuilders.min(agg.getName()).field(agg.getField());
	}

	@Override
	public AggregationNode parse(Aggregation aggregation) {
		Min min = (Min) aggregation;
		AggregationNode node = new AggregationNode();
		node.setName(min.getName());
		node.setValue(min.getValue());
		return node;
	}
}
