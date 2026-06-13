package com.reyco.dasbx.es.support.annotation.metadata;

import java.lang.reflect.Field;

import com.reyco.dasbx.es.support.annotation.routing.EsRouting;

public class RoutingMetadata {

	private Field field;
	private EsRouting annotation;

	public RoutingMetadata(Field field, EsRouting annotation) {
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

	public EsRouting getAnnotation() {
		return annotation;
	}

	public void setAnnotation(EsRouting annotation) {
		this.annotation = annotation;
	}
}
