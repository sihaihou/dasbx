package com.reyco.dasbx.es.core.query.geo.adapter;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapterRegistry;
import com.reyco.dasbx.es.core.query.geo.condition.GeoDistanceCondition;

public class GeoDistanceConditionAdapter implements ConditionAdapter<GeoDistanceCondition>{

	@Override
	public Class<GeoDistanceCondition> support() {
		return GeoDistanceCondition.class;
	}

	@Override
	public QueryBuilder adapt(GeoDistanceCondition condition, SearchContext context,
			ConditionAdapterRegistry registry) {
		return QueryBuilders
				.geoDistanceQuery(condition.getField())
				.point(condition.getLatitude(), condition.getLongitude())
				.distance(condition.getDistance(), condition.getUnit());
		 
	}

}
