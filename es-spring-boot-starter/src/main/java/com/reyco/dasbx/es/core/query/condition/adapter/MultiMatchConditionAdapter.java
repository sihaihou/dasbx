package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.MultiMatchCondition;

public class MultiMatchConditionAdapter implements ConditionAdapter<MultiMatchCondition> {

	@Override
	public Class<MultiMatchCondition> support() {
		return MultiMatchCondition.class;
	}

	@Override
	public QueryBuilder adapt(MultiMatchCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.multiMatchQuery(condition.getValue(),condition.getFields().toArray(new String[0]));
	}
}
