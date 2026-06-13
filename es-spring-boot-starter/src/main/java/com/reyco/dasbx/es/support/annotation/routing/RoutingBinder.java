package com.reyco.dasbx.es.support.annotation.routing;

import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.RoutingMetadata;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;

public class RoutingBinder {
	
	public void bind(Object dto, AnnotationMetadata metadata, EsExecutionContext context) {

		for (RoutingMetadata meta : metadata.getRoutings()) {
			try {
				Object value = meta.getField().get(dto);
				if (value == null) {
					continue;
				}

				context.setRouting(value.toString());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
