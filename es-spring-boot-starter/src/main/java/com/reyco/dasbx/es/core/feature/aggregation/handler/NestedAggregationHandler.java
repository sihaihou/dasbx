package com.reyco.dasbx.es.core.feature.aggregation.handler;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;

public class NestedAggregationHandler implements AggregationHandler {

	@Override
	public AggregationType type() {
		return AggregationType.NESTED;
	}

	@Override
	public AggregationBuilder build(AggregationDefinition agg) {
		return AggregationBuilders.nested(agg.getName(), agg.getField());
	}

	@Override
	public AggregationNode parse(Aggregation aggregation) {
		Nested nested = (Nested) aggregation;
		
		AggregationNode node = new AggregationNode();
		node.setType(AggregationType.NESTED);
		node.setName(nested.getName());
		return node;
	}

}
