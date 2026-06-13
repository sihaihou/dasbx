package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.RegexpCondition;

public class RegexpConditionAdapter implements ConditionAdapter<RegexpCondition> {

	@Override
	public Class<RegexpCondition> support() {
		return RegexpCondition.class;
	}

	@Override
	public QueryBuilder adapt(RegexpCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.regexpQuery(condition.getField(),condition.getValue());
	}
}
