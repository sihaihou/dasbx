package com.reyco.dasbx.es.core.query.condition.adapter;

import org.elasticsearch.index.query.QueryBuilder;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.Condition;

public interface ConditionAdapter<T extends Condition> {

	/**
	 * condition类型
	 */
	Class<T> support();

	/**
	 * 转ES QueryBuilder
	 */
	QueryBuilder adapt(T condition,SearchContext searchContext,ConditionAdapterRegistry registry);
	
}
