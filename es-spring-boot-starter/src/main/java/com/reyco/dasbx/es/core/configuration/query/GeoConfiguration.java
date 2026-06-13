package com.reyco.dasbx.es.core.configuration.query;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.es.core.query.geo.adapter.GeoBoundingBoxConditionAdapter;
import com.reyco.dasbx.es.core.query.geo.adapter.GeoDistanceConditionAdapter;
import com.reyco.dasbx.es.core.query.geo.adapter.GeoPolygonConditionAdapter;

@Configuration
public class GeoConfiguration {
	/**
	 * 查询
	 * @return
	 */
	@Bean
	public GeoDistanceConditionAdapter geoDistanceConditionAdapter() {
		return new GeoDistanceConditionAdapter();
	}
	@Bean
	public GeoBoundingBoxConditionAdapter geoBoundingBoxConditionAdapter() {
		return new GeoBoundingBoxConditionAdapter();
	}
	@Bean
	public GeoPolygonConditionAdapter geoPolygonConditionAdapter() {
		return new GeoPolygonConditionAdapter();
	}
	
}
