package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.MatchCondition;

public class MatchConditionAdapter implements ConditionAdapter<MatchCondition> {

	@Override
	public Class<MatchCondition> support() {
		return MatchCondition.class;
	}

	@Override
	public QueryBuilder adapt(MatchCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.matchQuery(condition.getField(),condition.getValue());
	}
}
