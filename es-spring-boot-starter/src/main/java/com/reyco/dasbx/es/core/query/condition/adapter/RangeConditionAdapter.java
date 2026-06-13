package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.RangeCondition;

public class RangeConditionAdapter implements ConditionAdapter<RangeCondition> {

	@Override
	public Class<RangeCondition> support() {
		return RangeCondition.class;
	}

	@Override
	public QueryBuilder adapt(RangeCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		RangeQueryBuilder builder = QueryBuilders.rangeQuery(condition.getField());
		if (condition.getGte() != null) {
			builder.gte(condition.getGte());
		}
		if (condition.getLte() != null) {
			builder.lte(condition.getLte());
		}
		if (condition.getGt() != null) {
			builder.gt(condition.getGt());
		}
		if (condition.getLt() != null) {
			builder.lt(condition.getLt());
		}
		return builder;
	}
}
