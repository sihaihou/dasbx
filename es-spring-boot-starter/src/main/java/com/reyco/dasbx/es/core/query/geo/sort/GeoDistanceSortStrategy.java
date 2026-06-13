package com.reyco.dasbx.es.core.query.geo.sort;

import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.sort.strategy.SortStrategy;

public class GeoDistanceSortStrategy implements SortStrategy<GeoDistanceSortDefinition> {

	@Override
	public Class<GeoDistanceSortDefinition> support() {
		return GeoDistanceSortDefinition.class;
	}

	@Override
	public void apply(GeoDistanceSortDefinition sort, SearchContext context) {
		GeoDistanceSortBuilder sortBuilder =SortBuilders
				.geoDistanceSort(
						sort.getField(),
						sort.getLatitude(),
						sort.getLongitude()
						)
				.order(sort.getSortOrder())
				.unit(sort.getUnit());
		context.getSourceBuilder().sort(sortBuilder);
	}
}
