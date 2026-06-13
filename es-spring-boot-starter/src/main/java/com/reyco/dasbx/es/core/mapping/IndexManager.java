package com.reyco.dasbx.es.core.mapping;

import com.reyco.dasbx.es.core.metadata.IndexMetadata;

public interface IndexManager {

	void createIfNotExists(String index, IndexMetadata metadata);

}