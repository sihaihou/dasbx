package com.reyco.dasbx.es.core.feature.aggregation.handler;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;

public class TermsAggregationHandler implements AggregationHandler {

	@Override
	public AggregationType type() {
		return AggregationType.TERMS;
	}

	@Override
	public AggregationBuilder build(AggregationDefinition agg) {
		return AggregationBuilders
				.terms(agg.getName())
				.field(agg.getResolvedField())
				.order(BucketOrder.key(true))
				.size(agg.getSize());
	}

	@Override
	public AggregationNode parse(Aggregation aggregation) {
		Terms terms = (Terms) aggregation;
		
		AggregationNode root = new AggregationNode();
		root.setType(AggregationType.TERMS);
		root.setName(terms.getName());
		
		for (Terms.Bucket bucket : terms.getBuckets()) {
			AggregationNode node = new AggregationNode();
			node.setKey(bucket.getKey());
			node.setType(AggregationType.TERMS);
			node.setDocCount(bucket.getDocCount());
			root.addChild(node);
		}
		return root;
	}
}
