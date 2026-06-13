package com.reyco.dasbx.es.support.annotation.metadata.parser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;

public class AnnotationMetadataRegistry {
	
	private final Map<Class<?>, AnnotationMetadata> cache = new ConcurrentHashMap<>();

	private AnnotationMetadataParser parser;

	public AnnotationMetadataRegistry() {
		this.parser = new AnnotationMetadataParser();
	}

	public AnnotationMetadata get(Class<?> clazz) {
		return cache.computeIfAbsent(clazz, parser::parse);
	}
}
