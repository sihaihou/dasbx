package com.reyco.dasbx.es.core.query.geo.sort;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.SortOrder;

import com.reyco.dasbx.es.core.query.sort.FieldSortDefinition;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;

public class GeoDistanceSortDefinition implements SortDefinition {
	/**
	 * field
	 */
	private String field;

	/**
	 * lat
	 */
	private Double latitude;

	/**
	 * lon
	 */
	private Double longitude;

	/**
	 * km/m
	 */
	private DistanceUnit unit = DistanceUnit.KILOMETERS;

	/**
	 * asc/desc
	 */
	private SortOrder sortOrder = SortOrder.ASC;
	
	private int order;
	
	public String getField() {
		return field;
	}

	public GeoDistanceSortDefinition setField(String field) {
		this.field = field;
		return this;
	}

	public Double getLatitude() {
		return latitude;
	}

	public GeoDistanceSortDefinition setLatitude(Double latitude) {
		this.latitude = latitude;
		return this;
	}

	public Double getLongitude() {
		return longitude;
	}

	public GeoDistanceSortDefinition setLongitude(Double longitude) {
		this.longitude = longitude;
		return this;
	}

	public DistanceUnit getUnit() {
		return unit;
	}

	public GeoDistanceSortDefinition setUnit(DistanceUnit unit) {
		this.unit = unit;
		return this;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public GeoDistanceSortDefinition setSortOrder(SortOrder SortOrder) {
		this.sortOrder = SortOrder;
		return this;
	}
	public GeoDistanceSortDefinition setOrder(int order) {
		this.order = order;
		return this;
	}
	@Override
	public int getOrder() {
		return order;
	}
}
