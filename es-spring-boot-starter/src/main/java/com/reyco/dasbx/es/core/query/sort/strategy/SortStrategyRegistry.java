package com.reyco.dasbx.es.core.query.sort.strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.reyco.dasbx.es.core.query.sort.SortDefinition;

public class SortStrategyRegistry {

	private final Map<Class<? extends SortDefinition>, SortStrategy<? extends SortDefinition>> strategies = new ConcurrentHashMap<>();

	public SortStrategyRegistry(List<SortStrategy<? extends SortDefinition>> sorts) {
		if (sorts == null) {
			return;
		}

		for (SortStrategy<? extends SortDefinition> strategy : sorts) {
			strategies.put(strategy.support(), strategy);
		}
	}

	public <T extends SortDefinition> SortStrategy<? extends SortDefinition> get(Class<T> clazz) {
		return strategies.get(clazz);
	}
}
