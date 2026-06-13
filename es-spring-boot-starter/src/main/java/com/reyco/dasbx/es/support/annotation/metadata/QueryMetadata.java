package com.reyco.dasbx.es.support.annotation.metadata;

import java.lang.reflect.Field;

import com.reyco.dasbx.es.support.annotation.query.EsQuery;

public class QueryMetadata {
	
	private Field field;

	private EsQuery annotation;

	public QueryMetadata(Field field, EsQuery annotation) {
		this.field = field;
		this.annotation = annotation;
	}

	public Field getField() {
		return field;
	}

	public EsQuery getAnnotation() {
		return annotation;
	}
}
