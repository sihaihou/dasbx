package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.WildcardCondition;

public class WildcardConditionAdapter implements ConditionAdapter<WildcardCondition> {

	@Override
	public Class<WildcardCondition> support() {
		return WildcardCondition.class;
	}

	@Override
	public QueryBuilder adapt(WildcardCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.wildcardQuery(condition.getField(),condition.getValue());
	}
}
