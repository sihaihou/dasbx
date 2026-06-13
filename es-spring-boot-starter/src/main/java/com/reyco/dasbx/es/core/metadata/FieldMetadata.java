package com.reyco.dasbx.es.core.metadata;

import java.util.EnumMap;
import java.util.Map;

public class FieldMetadata {
	/**
	 * java字段
	 */
	private String property;

	/**
	 * es字段
	 */
	private String field;

	/**
	 * keyword字段
	 */
	private String keywordField;
	/**
	 * ES字段类型
	 */
	private FieldType fieldType;
	/**
	 * 
	 */
	private Map<FieldCapability, String> resolvedFields = new EnumMap<>(FieldCapability.class);

	/**
	 * 嵌套
	 */
	private boolean nested;

	private String nestedPath;

	public String resolveField(FieldCapability capability) {
		String field = resolvedFields.get(capability);
		if (field != null) {
			return field;
		}
		return this.field;
	}

	public FieldMetadata addCapability(FieldCapability capability, String field) {
		resolvedFields.put(capability, field);
		return this;
	}

	public String getProperty() {
		return property;
	}

	public FieldMetadata setProperty(String property) {
		this.property = property;
		return this;
	}

	public String getField() {
		return field;
	}

	public FieldMetadata setField(String field) {
		this.field = field;
		return this;
	}

	public String getKeywordField() {
		return keywordField;
	}

	public FieldMetadata setKeywordField(String keywordField) {
		this.keywordField = keywordField;
		return this;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public FieldMetadata setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
		return this;
	}

	public boolean isNested() {
		return nested;
	}

	public FieldMetadata setNested(boolean nested) {
		this.nested = nested;
		return this;
	}

	public String getNestedPath() {
		return nestedPath;
	}

	public FieldMetadata setNestedPath(String nestedPath) {
		this.nestedPath = nestedPath;
		return this;
	}
}
