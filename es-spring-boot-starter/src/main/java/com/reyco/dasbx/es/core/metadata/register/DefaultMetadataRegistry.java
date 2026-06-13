package com.reyco.dasbx.es.core.metadata.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.reyco.dasbx.es.core.exception.SearchMappingException;
import com.reyco.dasbx.es.core.metadata.FieldMetadata;
import com.reyco.dasbx.es.core.metadata.IndexMetadata;

public class DefaultMetadataRegistry implements MetadataRegistry {

	private final Map<Class<?>, IndexMetadata> registry = new ConcurrentHashMap<>();

	@Override
	public void register(IndexMetadata metadata) {
		IndexMetadata old = registry.putIfAbsent(metadata.getEntityClass(), metadata);
		if (old != null) {
			throw new SearchMappingException("Metadata already registered : " + metadata.getEntityClass().getName());
		}
	}

	@Override
	public IndexMetadata get(Class<?> entityClass) {
		return registry.get(entityClass);
	}

	@Override
	public FieldMetadata getField(Class<?> entityClass, String property) {
		IndexMetadata metadata = get(entityClass);
		return metadata == null ? null : metadata.getField(property);
	}

	@Override
	public FieldMetadata requireField(Class<?> entityClass, String property) {
		FieldMetadata field = getField(entityClass, property);
		if (field == null) {
			throw new SearchMappingException("Field metadata not found : " + entityClass.getSimpleName() + "." + property);
		}
		return field;
	}

}
