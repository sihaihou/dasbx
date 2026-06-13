package com.reyco.dasbx.es.support.annotation.source;

import java.util.List;

import com.reyco.dasbx.es.support.annotation.SourceType;
import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.SourceMetadata;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;

public class SourceBinder {

	public void bind(Object dto, AnnotationMetadata metadata, EsExecutionContext context) {

		for (SourceMetadata meta : metadata.getSources()) {
			try {

				Object value = meta.getField().get(dto);
				if (value == null) {
					continue;
				}

				@SuppressWarnings("unchecked")
				List<String> fields = (List<String>) value;
				if (meta.getAnnotation().value() == SourceType.INCLUDE) {
					context.setIncludeFields(fields);

				} else {
					context.setExcludeFields(fields);
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
