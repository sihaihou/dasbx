package com.reyco.dasbx.es.core.query.condition.adapter;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.NestedCondition;

public class NestedConditionAdapter implements ConditionAdapter<NestedCondition> {

	@Override
	public Class<NestedCondition> support() {
		return NestedCondition.class;
	}

	@Override
	public QueryBuilder adapt(NestedCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders
				.nestedQuery(condition.getPath(),registry.adapt(condition.getCondition(),searchContext),ScoreMode.None);
	}
}
