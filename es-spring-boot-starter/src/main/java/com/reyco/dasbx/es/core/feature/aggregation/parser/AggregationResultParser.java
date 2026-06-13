package com.reyco.dasbx.es.core.feature.aggregation.parser;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.SingleBucketAggregation;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.feature.aggregation.handler.AggregationHandler;
import com.reyco.dasbx.es.core.feature.aggregation.handler.AggregationHandlerRegistry;

public class AggregationResultParser {

	private AggregationHandlerRegistry registry;

	public AggregationResultParser(AggregationHandlerRegistry registry) {
		super();
		this.registry = registry;
	}

	public List<AggregationNode> parse(Aggregations aggregations, List<AggregationDefinition> defs) {
		List<AggregationNode> result = new ArrayList<>();
		if (aggregations == null || defs == null) {
			return result;
		}

		for (AggregationDefinition def : defs) {
			Aggregation aggregation = aggregations.get(def.getName());
			if (aggregation == null) {
				continue;
			}
			AggregationNode node = parseNode(aggregation, def);
			result.add(node);
		}

		return result;
	}

	private AggregationNode parseNode(Aggregation aggregation, AggregationDefinition def) {
		AggregationHandler handler = registry.get(def.getType());
		if (handler == null) {
			throw new RuntimeException("unsupported aggregation: " + def.getType());
		}

		AggregationNode root = handler.parse(aggregation);
		/*
		 * 子聚合递归
		 */
		if (def.getChildren() == null || def.getChildren().isEmpty()) {
			return root;
		}

		/*
		 * bucket聚合
		 */
		if (aggregation instanceof MultiBucketsAggregation) {
			MultiBucketsAggregation buckets = (MultiBucketsAggregation) aggregation;

			List<? extends MultiBucketsAggregation.Bucket> bucketList = buckets.getBuckets();
			List<AggregationNode> children = root.getChildren();

			for (int i = 0; i < bucketList.size(); i++) {
				MultiBucketsAggregation.Bucket bucket = bucketList.get(i);
				AggregationNode bucketNode = children.get(i);

				for (AggregationDefinition childDef : def.getChildren()) {
					Aggregation childAgg = bucket.getAggregations().get(childDef.getName());
					if (childAgg == null) {
						continue;
					}
					AggregationNode childNode = parseNode(childAgg, childDef);
					bucketNode.addChild(childNode);
				}
			}
		} else if (aggregation instanceof SingleBucketAggregation) {
			SingleBucketAggregation bucket = (SingleBucketAggregation) aggregation;
			for (AggregationDefinition childDef : def.getChildren()) {
				Aggregation childAgg = bucket.getAggregations().get(childDef.getName());
				if (childAgg == null) {
					continue;
				}
				AggregationNode childNode = parseNode(childAgg, childDef);
				root.addChild(childNode);
			}
		}
		return root;
	}
}
