package com.reyco.dasbx.es.support.annotation.metadata;

import java.lang.reflect.Field;

import com.reyco.dasbx.es.support.annotation.source.EsSource;

public class SourceMetadata {

	private Field field;

	private EsSource annotation;

	public SourceMetadata(Field field, EsSource annotation) {
		super();
		this.field = field;
		this.annotation = annotation;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public EsSource getAnnotation() {
		return annotation;
	}

	public void setAnnotation(EsSource annotation) {
		this.annotation = annotation;
	}
	
}
