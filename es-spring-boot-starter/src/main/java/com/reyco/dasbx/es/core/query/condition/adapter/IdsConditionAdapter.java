package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.IdsCondition;

public class IdsConditionAdapter implements ConditionAdapter<IdsCondition> {

	@Override
	public Class<IdsCondition> support() {
		return IdsCondition.class;
	}

	@Override
	public QueryBuilder adapt(IdsCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.idsQuery().addIds(condition.getIds().toArray(new String[0]));
	}
}
