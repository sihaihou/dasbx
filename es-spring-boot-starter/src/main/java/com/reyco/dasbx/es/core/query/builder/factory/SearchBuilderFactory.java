package com.reyco.dasbx.es.core.query.builder.factory;

import com.reyco.dasbx.es.core.query.builder.SearchBuilder;

public interface SearchBuilderFactory {

	<T> SearchBuilder<T> builder(Class<T> clazz);

}