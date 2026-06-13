package com.reyco.dasbx.es.core.query.geo;

import java.util.List;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.SortOrder;

import com.reyco.dasbx.es.core.query.geo.condition.GeoBoundingBoxCondition;
import com.reyco.dasbx.es.core.query.geo.condition.GeoDistanceCondition;
import com.reyco.dasbx.es.core.query.geo.condition.GeoPolygonCondition;
import com.reyco.dasbx.es.core.query.geo.sort.GeoDistanceSortDefinition;

public class Geos {
	
	private Geos() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Geo排序
	 * @param field
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public GeoDistanceSortDefinition geoDistanceSort(String field,Double latitude,Double longitude) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude);
		return sort;
	}
	public GeoDistanceSortDefinition geoDistanceSort(String field,Double latitude,Double longitude,SortOrder order) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setSortOrder(order);
		return sort;
	}
	public GeoDistanceSortDefinition geoDistanceSort(String field,Double latitude,Double longitude,DistanceUnit unit) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setUnit(unit);
		return sort;
	}
	public GeoDistanceSortDefinition geoDistanceSort(String field,Double latitude,Double longitude,DistanceUnit unit,SortOrder order) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setUnit(unit)
				.setSortOrder(order);
		return sort;
	}
	
	
	
	/**
	 * Geo查询
	 * @param field
	 * @param latitude
	 * @param longitude
	 * @param distance
	 * @return
	 */
	
	public GeoDistanceCondition GeoDistanceCondition(String field,Double latitude,Double longitude,Double distance) {
		GeoDistanceCondition sort = new GeoDistanceCondition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setDistance(distance);
		return sort;
	}
	public GeoDistanceCondition GeoDistanceCondition(String field,Double latitude,Double longitude,Double distance,SortOrder order) {
		GeoDistanceCondition sort = new GeoDistanceCondition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setDistance(distance)
				.setOrder(order);
		return sort;
	}
	public GeoDistanceCondition GeoDistanceCondition(String field,Double latitude,Double longitude,Double distance,DistanceUnit unit,SortOrder order) {
		GeoDistanceCondition sort = new GeoDistanceCondition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setDistance(distance)
				.setUnit(unit)
				.setOrder(order);
		return sort;
	}
	
	
	public GeoBoundingBoxCondition GeoBoundingBox(String field, Double top, Double left, Double bottom, Double right) {
		GeoBoundingBoxCondition geoBoundingBoxCondition = new GeoBoundingBoxCondition()
				.setField(field)
				.setTop(top)
				.setLeft(left)
				.setBottom(bottom)
				.setRight(right);
		return geoBoundingBoxCondition;

	}
	
	public GeoPolygonCondition geoPolygonCondition(String field,List<GeoPoint> points) {
		GeoPolygonCondition geoBoundingBoxCondition = new GeoPolygonCondition()
				.setField(field)
				.setPoints(points);
		return geoBoundingBoxCondition;

	}
	
}
