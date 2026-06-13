package com.reyco.dasbx.es.core.query.condition;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.SortOrder;

import com.reyco.dasbx.es.core.query.geo.GeoPoint;
import com.reyco.dasbx.es.core.query.geo.condition.GeoBoundingBoxCondition;
import com.reyco.dasbx.es.core.query.geo.condition.GeoDistanceCondition;
import com.reyco.dasbx.es.core.query.geo.condition.GeoPolygonCondition;

/**
 * Condition工具栏-----负责生成各种condition对象
 * @author reyco
 *
 */
public class Querys {
	
	private Querys() {
		// TODO Auto-generated constructor stub
	}

	public static BoolCondition bool() {
		return new BoolCondition();
	}

	public static TermCondition term(String field, Object value) {
		return new TermCondition(field, value);
	}

	public static TermsCondition terms(String field, Collection<?> values) {
		return new TermsCondition(field, values);
	}

	public static MatchCondition match(String field, Object value) {
		return new MatchCondition(field, value);
	}

	public static MultiMatchCondition multiMatch(Object value, String... fields) {
		return new MultiMatchCondition(value, Arrays.asList(fields));
	}
	
	public static RangeCondition range(String field) {
		return new RangeCondition(field);
	}

	public static WildcardCondition wildcard(String field, String value) {
		return new WildcardCondition(field, value);
	}

	public static NestedCondition nested(String path, Condition condition) {
		return new NestedCondition(path, condition);
	}

	public static PrefixCondition prefix(String field, String value) {
		return new PrefixCondition(field, value);
	}

	public static RegexpCondition regexp(String field, String value) {
		return new RegexpCondition(field, value);
	}

	public static FuzzyCondition fuzzy(String field, Object value) {
		return new FuzzyCondition(field, value);
	}

	public static IdsCondition ids(List<String> ids) {
		return new IdsCondition(ids);
	}

	public static ExistsCondition exists(String field) {
		return new ExistsCondition(field);
	}
	
	/**
	 * geo查询
	 * @param field
	 * @param latitude
	 * @param longitude
	 * @param distance
	 * @return
	 */
	public static GeoDistanceCondition geoDistance(String field,Double latitude,Double longitude,Double distance) {
		GeoDistanceCondition sort = new GeoDistanceCondition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setDistance(distance);
		return sort;
	}
	public static GeoDistanceCondition geoDistance(String field,Double latitude,Double longitude,Double distance,SortOrder order) {
		GeoDistanceCondition sort = new GeoDistanceCondition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setDistance(distance)
				.setOrder(order);
		return sort;
	}
	public static GeoDistanceCondition geoDistance(String field,Double latitude,Double longitude,Double distance,DistanceUnit unit,SortOrder order) {
		GeoDistanceCondition sort = new GeoDistanceCondition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setDistance(distance)
				.setUnit(unit)
				.setOrder(order);
		return sort;
	}
	public static GeoBoundingBoxCondition geoBoundingBox(String field, Double top, Double left, Double bottom, Double right) {
		GeoBoundingBoxCondition geoBoundingBoxCondition = new GeoBoundingBoxCondition()
				.setField(field)
				.setTop(top)
				.setLeft(left)
				.setBottom(bottom)
				.setRight(right);
		return geoBoundingBoxCondition;

	}
	public static GeoPolygonCondition geoPolygon(String field,List<GeoPoint> points) {
		GeoPolygonCondition geoBoundingBoxCondition = new GeoPolygonCondition()
				.setField(field)
				.setPoints(points);
		return geoBoundingBoxCondition;

	}
	
}
