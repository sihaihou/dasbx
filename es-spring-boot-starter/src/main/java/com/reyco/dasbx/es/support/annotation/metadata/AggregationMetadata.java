package com.reyco.dasbx.es.support.annotation.metadata;

import java.lang.reflect.Field;
import java.util.List;

import com.reyco.dasbx.es.support.annotation.aggregation.EsAgg;

public class AggregationMetadata {

	private Field field;

	private List<EsAgg> annotations;

	public AggregationMetadata(Field field, List<EsAgg> annotations) {
		this.field = field;
		this.annotations = annotations;
	}

	public Field getField() {
		return field;
	}

	public List<EsAgg> getAnnotations() {
		return annotations;
	}

}
