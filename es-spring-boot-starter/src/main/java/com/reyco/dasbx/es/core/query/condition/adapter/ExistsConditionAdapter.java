package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.ExistsCondition;

public class ExistsConditionAdapter implements ConditionAdapter<ExistsCondition> {

	@Override
	public Class<ExistsCondition> support() {
		return ExistsCondition.class;
	}

	@Override
	public QueryBuilder adapt(ExistsCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.existsQuery(condition.getField());
	}
}
