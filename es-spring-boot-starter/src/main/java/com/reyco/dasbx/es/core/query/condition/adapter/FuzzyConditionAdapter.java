package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.FuzzyCondition;

public class FuzzyConditionAdapter implements ConditionAdapter<FuzzyCondition> {

	@Override
	public Class<FuzzyCondition> support() {
		return FuzzyCondition.class;
	}

	@Override
	public QueryBuilder adapt(FuzzyCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.fuzzyQuery(condition.getField(),condition.getValue());
	}
}
