package com.reyco.dasbx.es.core.mapping;

import java.util.Map;

import com.reyco.dasbx.es.core.metadata.IndexMetadata;

public interface MappingBuilder {

	Map<String, Object> build(IndexMetadata metadata);

}