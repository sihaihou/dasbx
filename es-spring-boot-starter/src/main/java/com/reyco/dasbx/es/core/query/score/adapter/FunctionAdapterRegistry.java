package com.reyco.dasbx.es.core.query.score.adapter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.query.score.ScoreFunction;

public class FunctionAdapterRegistry {

	private final Map<Class<?>, FunctionAdapter<?>> adapterMap = new ConcurrentHashMap<>();

	public FunctionAdapterRegistry(List<FunctionAdapter<?>> adapters) {
		for (FunctionAdapter<?> adapter : adapters) {
			Class<?> support = adapter.support();
			if (adapterMap.containsKey(support)) {
				throw new SearchBuildException("Duplicate FunctionAdapter : " + support.getName());
			}
			adapterMap.put(support, adapter);
		}
	}

	public ScoreFunctionBuilder<?> adapt(ScoreFunction function) {
		FunctionAdapter adapter = adapterMap.get(function.getClass());
		if (adapter != null) {
			throw new RuntimeException("FunctionAdapter not found : " + function.getClass());
		}
		return adapter.adapt(function);
	}
}
