package com.reyco.dasbx.es.core.query.builder.factory;

import com.reyco.dasbx.es.core.engine.SearchEngine;
import com.reyco.dasbx.es.core.query.builder.DefaultSearchBuilder;
import com.reyco.dasbx.es.core.query.builder.SearchBuilder;
import com.reyco.dasbx.es.core.result.mapper.ResultMapper;

public class DefaultSearchBuilderFactory implements SearchBuilderFactory {
	
	private SearchEngine engine;

	private ResultMapper mapper;
	
	public DefaultSearchBuilderFactory(SearchEngine engine, ResultMapper mapper) {
		super();
		this.engine = engine;
		this.mapper = mapper;
	}

	@Override
	public <T> SearchBuilder<T> builder(Class<T> clazz) {
		 return new DefaultSearchBuilder<>(clazz,engine,mapper);
	}

}
