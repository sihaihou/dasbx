package com.reyco.dasbx.es.core.metadata;

import java.util.HashMap;
import java.util.Map;

import com.reyco.dasbx.es.core.exception.SearchMappingException;

public class IndexMetadata {

	private Class<?> entityClass;

	private Map<String, FieldMetadata> fields = new HashMap<>();
	
	/**
	 * 添加属性
	 * @param metadata
	 */
	public void addField(FieldMetadata metadata) {
		FieldMetadata old = fields.putIfAbsent(metadata.getProperty(), metadata);
		if (old != null) {
			throw new SearchMappingException("Duplicate field metadata : " + metadata.getProperty());
		}
	}

	public Map<String, FieldMetadata> getFields() {
		return fields;
	}

	public FieldMetadata getField(String property) {
		return fields.get(property);
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

}
