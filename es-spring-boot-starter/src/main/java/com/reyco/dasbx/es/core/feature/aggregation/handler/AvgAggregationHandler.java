package com.reyco.dasbx.es.core.feature.aggregation.handler;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Avg;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;

public class AvgAggregationHandler implements AggregationHandler {

	@Override
	public AggregationType type() {
		return AggregationType.AVG;
	}

	@Override
	public AggregationBuilder build(AggregationDefinition agg) {
		return AggregationBuilders.avg(agg.getName()).field(agg.getField());
	}

	@Override
	public AggregationNode parse(Aggregation aggregation) {
		Avg avg = (Avg) aggregation;
		
		AggregationNode node = new AggregationNode();
		node.setType(AggregationType.AVG);
		node.setName(avg.getName());
		node.setValue(avg.getValue());
		return node;
	}
}
