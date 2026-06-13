package com.reyco.dasbx.es.core.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import com.reyco.dasbx.es.core.metadata.FieldMetadata;
import com.reyco.dasbx.es.core.metadata.FieldType;
import com.reyco.dasbx.es.core.metadata.IndexMetadata;

public class DefaultMappingBuilder implements MappingBuilder {

	@Override
	public Map<String, Object> build(IndexMetadata metadata) {
		Map<String, Object> root = new LinkedHashMap<>();
		Map<String, Object> properties = new LinkedHashMap<>();
		root.put("properties", properties);
		for (FieldMetadata field : metadata.getFields().values()) {
			Map<String, Object> fieldMap = buildField(field);
			properties.put(field.getField(), fieldMap);
		}
		return root;
	}

	private Map<String, Object> buildField(FieldMetadata field) {
		Map<String, Object> mapping = new LinkedHashMap<>();
		//type
		if (field.getFieldType() != null) {
			mapping.put("type", field.getFieldType().getType());
		}
		//keyword sub field
		if (field.getKeywordField() != null && field.getFieldType() == FieldType.TEXT) {
			Map<String, Object> fields = new LinkedHashMap<>();
			Map<String, Object> keyword = new LinkedHashMap<>();
			keyword.put("type", "keyword");
			String keywordName = field.getKeywordField().replace(field.getField() + ".", "");
			fields.put(keywordName, keyword);
			mapping.put("fields", fields);
		}
		return mapping;
	}
}
