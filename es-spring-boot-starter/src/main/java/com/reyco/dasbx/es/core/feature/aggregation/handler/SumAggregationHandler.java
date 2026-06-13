package com.reyco.dasbx.es.core.feature.aggregation.handler;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Sum;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;

public class SumAggregationHandler implements AggregationHandler {

	@Override
	public AggregationType type() {
		return AggregationType.SUM;
	}

	@Override
	public AggregationBuilder build(AggregationDefinition agg) {
		return AggregationBuilders.sum(agg.getName()).field(agg.getField());
	}

	@Override
	public AggregationNode parse(Aggregation aggregation) {
		Sum sum = (Sum) aggregation;
		AggregationNode node = new AggregationNode();
		node.setName(sum.getName());
		node.setValue(sum.getValue());
		return node;
	}
}
