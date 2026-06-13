package com.reyco.dasbx.es.core.query.condition.adapter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.index.query.QueryBuilder;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.Condition;

public class ConditionAdapterRegistry {

	private final Map<Class<? extends Condition>, ConditionAdapter<?>> adapterMap = new ConcurrentHashMap<>();

	public ConditionAdapterRegistry(List<ConditionAdapter<?>> adapters) {
		for (ConditionAdapter<?> adapter : adapters) {
			Class<? extends Condition> support = adapter.support();
			if (adapterMap.containsKey(support)) {
				throw new SearchBuildException("Duplicate ConditionAdapter : " + support.getName());
			}
			adapterMap.put(support, adapter);
		}
	}

	/**
	 * 适配condition
	 */
	@SuppressWarnings("unchecked")
	public QueryBuilder adapt(Condition condition, SearchContext context) {
		if (condition == null || !condition.isValid()) {
			return null;
		}
		
		ConditionAdapter adapter = adapterMap.get(condition.getClass());
		if (adapter == null) {
			throw new SearchBuildException("ConditionAdapter not found : " + condition.getClass());
		}

		return adapter.adapt(condition, context, this);
	}
}
