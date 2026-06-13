package com.reyco.dasbx.es.support.annotation.query;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.SortOrder;

public class EsGeoDistanceParam {
	
	private Double latitude;

	private Double longitude;
	
	private Double distance;
	
	private DistanceUnit unit = DistanceUnit.KILOMETERS;
	
	private SortOrder order = SortOrder.ASC;

	public Double getLatitude() {
		return latitude;
	}

	public EsGeoDistanceParam setLatitude(Double latitude) {
		this.latitude = latitude;
		return this;
	}

	public Double getLongitude() {
		return longitude;
	}

	public EsGeoDistanceParam setLongitude(Double longitude) {
		this.longitude = longitude;
		return this;
	}

	public Double getDistance() {
		return distance;
	}

	public EsGeoDistanceParam setDistance(Double distance) {
		this.distance = distance;
		return this;
	}
	
	public DistanceUnit getUnit() {
		return unit;
	}

	public EsGeoDistanceParam setUnit(DistanceUnit unit) {
		this.unit = unit;
		return this;
	}

	public SortOrder getOrder() {
		return order;
	}

	public EsGeoDistanceParam setOrder(SortOrder order) {
		this.order = order;
		return this;
	}
}
