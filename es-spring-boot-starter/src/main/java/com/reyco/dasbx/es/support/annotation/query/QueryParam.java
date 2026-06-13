package com.reyco.dasbx.es.support.annotation.query;

public class QueryParam {
	final String field;
	final Object value;
	final EsQuery annotation;

	QueryParam(String field, Object value, EsQuery annotation) {
		this.field = field;
		this.value = value;
		this.annotation = annotation;
	}
}
