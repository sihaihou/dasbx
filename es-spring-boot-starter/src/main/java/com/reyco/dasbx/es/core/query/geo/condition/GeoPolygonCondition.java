package com.reyco.dasbx.es.core.query.geo.condition;

import java.util.ArrayList;
import java.util.List;

import com.reyco.dasbx.es.core.query.condition.AbstractCondition;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.geo.GeoPoint;

public class GeoPolygonCondition extends AbstractCondition {
	/**
	 * geo field
	 */
	private String field;

	/**
	 * polygon points
	 */
	private List<GeoPoint> points = new ArrayList<>();

	@Override
	public boolean isValid() {
	    return points != null
	            && points.size() >= 3;
	}
	
	public String getField() {
		return field;
	}

	public GeoPolygonCondition setField(String field) {
		this.field = field;
		return this;
	}

	public List<GeoPoint> getPoints() {
		return points;
	}

	public GeoPolygonCondition setPoints(List<GeoPoint> points) {
		this.points = points;
		return this;
	}
}
