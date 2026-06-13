package com.reyco.dasbx.es.core.metadata.register;

import com.reyco.dasbx.es.core.metadata.FieldMetadata;
import com.reyco.dasbx.es.core.metadata.IndexMetadata;

public interface MetadataRegistry {

	void register(IndexMetadata metadata);

	IndexMetadata get(Class<?> entityClass);

	FieldMetadata getField(Class<?> entityClass, String property);

	FieldMetadata requireField(Class<?> entityClass, String property);
	
}
