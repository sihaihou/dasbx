package com.reyco.dasbx.es.core.pipeline.feature;

import java.util.List;

import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.aggregation.factory.AggregationBuilderFactory;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;

public class AggregationPipeline implements SearchPipeline {

	private AggregationBuilderFactory factory;

	public AggregationPipeline(AggregationBuilderFactory factory) {
		super();
		this.factory = factory;
	}

	@Override
	public void execute(SearchContext context) {
		try {
			List<AggregationDefinition> aggs = context.getQuery().getAggregations();
			if (aggs == null || aggs.isEmpty()) {
				return;
			}
			SearchSourceBuilder source = context.getSourceBuilder();
			for (AggregationDefinition agg : aggs) {
				source.aggregation(factory.build(agg, context));
			}
		}catch (Exception e) {
			throw new SearchBuildException("Aggregation build error!",e);
		}
	}

	@Override
	public int getOrder() {
		return 50;
	}
}
