package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.condition.Condition;

public class BoolConditionAdapter implements ConditionAdapter<BoolCondition> {

	@Override
	public Class<BoolCondition> support() {
		return BoolCondition.class;
	}

	@Override
	public QueryBuilder adapt(BoolCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		for (Condition item : condition.getMust()) {
			boolQuery.must(registry.adapt(item,searchContext));
		}

		for (Condition item : condition.getShould()) {
			boolQuery.should(registry.adapt(item,searchContext));
		}

		for (Condition item : condition.getFilter()) {
			boolQuery.filter(registry.adapt(item,searchContext));
		}

		for (Condition item : condition.getMustNot()) {
			boolQuery.mustNot(registry.adapt(item,searchContext));
		}

		return boolQuery;
	}
}
