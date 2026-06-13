package com.reyco.dasbx.es.core.query.geo.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapterRegistry;
import com.reyco.dasbx.es.core.query.geo.GeoPoint;
import com.reyco.dasbx.es.core.query.geo.condition.GeoPolygonCondition;

public class GeoPolygonConditionAdapter implements ConditionAdapter<GeoPolygonCondition> {

	@Override
	public Class<GeoPolygonCondition> support() {
		return GeoPolygonCondition.class;
	}

	@Override
	public QueryBuilder adapt(GeoPolygonCondition condition, SearchContext context, ConditionAdapterRegistry registry) {
		List<org.elasticsearch.common.geo.GeoPoint> points = convert(condition.getPoints());
		return QueryBuilders.geoPolygonQuery(condition.getField(), points);
	}

	private List<org.elasticsearch.common.geo.GeoPoint> convert(List<GeoPoint> points) {
		if (points == null || points.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<org.elasticsearch.common.geo.GeoPoint> result = new ArrayList<>(points.size());
		for (GeoPoint point : points) {
			result.add(new org.elasticsearch.common.geo.GeoPoint(point.getLatitude(), point.getLongitude()));
		}

		return result;
	}
}
