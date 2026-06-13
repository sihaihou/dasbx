package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.PrefixCondition;

public class PrefixConditionAdapter implements ConditionAdapter<PrefixCondition> {

	@Override
	public Class<PrefixCondition> support() {
		return PrefixCondition.class;
	}

	@Override
	public QueryBuilder adapt(PrefixCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.prefixQuery(condition.getField(),condition.getValue());
	}
}
