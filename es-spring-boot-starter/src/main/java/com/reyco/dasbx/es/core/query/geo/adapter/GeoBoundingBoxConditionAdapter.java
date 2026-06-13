package com.reyco.dasbx.es.core.query.geo.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapterRegistry;
import com.reyco.dasbx.es.core.query.geo.condition.GeoBoundingBoxCondition;

public class GeoBoundingBoxConditionAdapter implements ConditionAdapter<GeoBoundingBoxCondition> {

	@Override
	public Class<GeoBoundingBoxCondition> support() {
		return GeoBoundingBoxCondition.class;
	}

	@Override
	public QueryBuilder adapt(GeoBoundingBoxCondition condition,SearchContext context,ConditionAdapterRegistry registry) {
		return QueryBuilders
				.geoBoundingBoxQuery(condition.getField())
				.setCorners(condition.getTop(),condition.getLeft(),condition.getBottom(),condition.getRight());
	}
}
