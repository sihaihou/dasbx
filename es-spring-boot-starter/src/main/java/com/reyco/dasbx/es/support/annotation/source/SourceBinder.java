package com.reyco.dasbx.es.support.annotation.source;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.reyco.dasbx.es.support.annotation.SourceType;
import com.reyco.dasbx.es.support.annotation.execptio.SearchAnnotationException;
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
				if (!(value instanceof List)) {
				    throw new SearchAnnotationException("@EsSource must be annotated on a List<String> field");
				}
				List<?> rawFields = (List<?>) value;
				List<String> fields = new ArrayList<>(rawFields.size());
				for (Object item : rawFields) {
				    if (!(item instanceof String)
				            || !StringUtils.hasText((String) item)) {
				        throw new SearchAnnotationException("@EsSource list must contain non-empty String values");
				    }
				    fields.add((String) item);
				}
				if (meta.getAnnotation().value() == SourceType.INCLUDE) {
				    List<String> includes = context.getIncludeFields();
				    if (includes == null) {
				        includes = new ArrayList<>();
				        context.setIncludeFields(includes);
				    }
				    includes.addAll(fields);
				} else {
				    List<String> excludes = context.getExcludeFields();
				    if (excludes == null) {
				        excludes = new ArrayList<>();
				        context.setExcludeFields(excludes);
				    }
				    excludes.addAll(fields);
				}
			}catch (SearchAnnotationException e) {
			    throw e;
			} catch (Exception e) {
			    throw new SearchAnnotationException("@EsSource bind error", e);
			}
		}
	}
}
