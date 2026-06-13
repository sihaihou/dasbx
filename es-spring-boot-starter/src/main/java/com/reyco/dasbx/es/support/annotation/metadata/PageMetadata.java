package com.reyco.dasbx.es.support.annotation.metadata;

import java.lang.reflect.Field;

import com.reyco.dasbx.es.support.annotation.page.EsPage;

public class PageMetadata {
	
	private Field field;
	
	private EsPage annotation;
	
	public PageMetadata(Field field, EsPage annotation) {
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

	public EsPage getAnnotation() {
		return annotation;
	}

	public void setAnnotation(EsPage annotation) {
		this.annotation = annotation;
	}
	
}
