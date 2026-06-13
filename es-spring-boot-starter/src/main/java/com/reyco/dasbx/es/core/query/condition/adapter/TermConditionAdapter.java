package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.TermCondition;

public class TermConditionAdapter implements ConditionAdapter<TermCondition> {

	@Override
	public Class<TermCondition> support() {
		return TermCondition.class;
	}

	@Override
	public QueryBuilder adapt(TermCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.termQuery(condition.getField(),condition.getValue());
	}
}
