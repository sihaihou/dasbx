package com.reyco.dasbx.es.support.annotation.metadata;

import java.lang.reflect.Field;

import com.reyco.dasbx.es.support.annotation.highlight.EsHighlightField;

public class HighlightMetadata {
	
	private Field field;

	private EsHighlightField annotation;

	public HighlightMetadata(Field field, EsHighlightField annotation) {

		this.field = field;
		this.annotation = annotation;
	}

	public Field getField() {
		return field;
	}

	public EsHighlightField getAnnotation() {
		return annotation;
	}
}
