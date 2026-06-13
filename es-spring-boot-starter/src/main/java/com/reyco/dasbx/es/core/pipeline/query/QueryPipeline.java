package com.reyco.dasbx.es.core.pipeline.query;

import org.elasticsearch.index.query.QueryBuilder;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapterRegistry;

public class QueryPipeline implements SearchPipeline {

	private ConditionAdapterRegistry registry;

	public QueryPipeline(ConditionAdapterRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void execute(SearchContext context) {
		try {
			Condition condition = context.getCondition();
			if (condition == null) {
				return;
			}
			QueryBuilder queryBuilder = registry.adapt(condition,context);
			context.getSourceBuilder().query(queryBuilder);
		}catch (Exception e) {
			throw new SearchBuildException("Query build error!",e);
		}
	}
	
	@Override
	public int getOrder() {
		return 30;
	}
}
