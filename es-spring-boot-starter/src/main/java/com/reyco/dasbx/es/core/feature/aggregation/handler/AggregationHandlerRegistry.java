package com.reyco.dasbx.es.core.feature.aggregation.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationType;

public class AggregationHandlerRegistry {
	
	private final Map<AggregationType, AggregationHandler> handlerMap = new ConcurrentHashMap<>();

	public AggregationHandlerRegistry(List<AggregationHandler> handlers) {
		for (AggregationHandler handler : handlers) {
			handlerMap.put(handler.type(), handler);
		}
	}

	public AggregationHandler get(AggregationType type) {
		return handlerMap.get(type);
	}
}
