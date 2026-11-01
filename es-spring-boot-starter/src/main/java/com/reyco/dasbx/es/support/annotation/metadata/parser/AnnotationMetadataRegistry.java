package com.reyco.dasbx.es.support.annotation.metadata.parser;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;

public class AnnotationMetadataRegistry {

	private final Map<Class<?>, AnnotationMetadata> cache = new ConcurrentHashMap<>();

	private final AnnotationMetadataParser parser;

	public AnnotationMetadataRegistry(AnnotationMetadataParser parser) {
		this.parser = Objects.requireNonNull(parser, "AnnotationMetadataParser must not be null");
	}

	public AnnotationMetadata get(Class<?> clazz) {
		return cache.computeIfAbsent(clazz, parser::parse);
	}
}
