package com.reyco.dasbx.es.support.annotation.metadata;

import java.lang.reflect.Field;

import com.reyco.dasbx.es.support.annotation.sort.EsSort;

public class SortMetadata {
	
	private Field field;

	private EsSort annotation;

	public SortMetadata(Field field, EsSort annotation) {

		this.field = field;
		this.annotation = annotation;
	}

	public Field getField() {
		return field;
	}

	public EsSort getAnnotation() {
		return annotation;
	}
}
