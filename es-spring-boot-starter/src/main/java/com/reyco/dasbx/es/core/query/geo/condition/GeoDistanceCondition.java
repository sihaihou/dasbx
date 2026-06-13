package com.reyco.dasbx.es.core.query.geo.condition;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.SortOrder;

import com.reyco.dasbx.es.core.query.condition.AbstractCondition;

public class GeoDistanceCondition extends AbstractCondition{
	/**
	 * field
	 */
	private String field;
	/**
	 * 纬度
	 */
	private Double latitude;

	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 距离
	 */
	private Double distance;
	/**
	 * km/m
	 */
	private DistanceUnit unit = DistanceUnit.KILOMETERS;

	/**
	 * asc/desc
	 */
	private SortOrder order = SortOrder.ASC;

	@Override
	public boolean isValid() {
	    return latitude != null
	            && longitude != null
	            && distance != null;
	}
	
	public String getField() {
		return field;
	}
	public GeoDistanceCondition setField(String field) {
		this.field = field;
		return this;
	}
	public Double getLatitude() {
		return latitude;
	}
	public GeoDistanceCondition setLatitude(Double latitude) {
		this.latitude = latitude;
		return this;
	}
	public Double getLongitude() {
		return longitude;
	}
	public GeoDistanceCondition setLongitude(Double longitude) {
		this.longitude = longitude;
		return this;
	}
	public Double getDistance() {
		return distance;
	}
	public GeoDistanceCondition setDistance(Double distance) {
		this.distance = distance;
		return this;
	}
	public DistanceUnit getUnit() {
		return unit;
	}
	public GeoDistanceCondition setUnit(DistanceUnit unit) {
		this.unit = unit;
		return this;
	}
	public SortOrder getOrder() {
		return order;
	}
	public GeoDistanceCondition setOrder(SortOrder order) {
		this.order = order;
		return this;
	}
}
