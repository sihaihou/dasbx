package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.TermsCondition;

public class TermsConditionAdapter implements ConditionAdapter<TermsCondition> {

	@Override
	public Class<TermsCondition> support() {
		return TermsCondition.class;
	}

	@Override
	public QueryBuilder adapt(TermsCondition condition,SearchContext searchContext,ConditionAdapterRegistry registry) {
		return QueryBuilders.termsQuery(condition.getField(),condition.getValues());
	}

}
