package com.reyco.dasbx.es.core.query.sort;

import java.util.Map;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.ScriptSortBuilder.ScriptSortType;

import com.reyco.dasbx.es.core.query.geo.sort.GeoDistanceSortDefinition;

public class Sorts {
	
	private Sorts() {
	}
	
	public static FieldSortDefinition field(String field) {
		FieldSortDefinition sort = new FieldSortDefinition()
				.setField(field);
		return sort;
	}
	public static FieldSortDefinition field(String field,SortOrder sortOrder) {
		FieldSortDefinition sort = new FieldSortDefinition()
				.setField(field)
				.setSortOrder(sortOrder);
		return sort;
	}
	public static FieldSortDefinition field(String field,SortOrder sortOrder,int order) {
		FieldSortDefinition sort = new FieldSortDefinition()
				.setField(field)
				.setSortOrder(sortOrder)
				.setOrder(order);
		return sort;
	}
	
	
	public static ScoreSortDefinition score(){
		return new ScoreSortDefinition();
	}
	public static ScoreSortDefinition score(SortOrder sortOrder){
		return new ScoreSortDefinition()
				.setSortOrder(sortOrder);
	}
	public static ScoreSortDefinition score(SortOrder sortOrder,int order){
		return new ScoreSortDefinition()
				.setSortOrder(sortOrder)
				.setOrder(order);
	}
	
	public static ScriptSortDefinition script(String script){
		return new ScriptSortDefinition()
				.setScript(script);
	}
	public static ScriptSortDefinition script(String script,SortOrder order){
		return new ScriptSortDefinition()
				.setScript(script)
				.setSortOrder(order);
	}
	public static ScriptSortDefinition script(String script,Map<String, Object> params,SortOrder order){
		return new ScriptSortDefinition()
				.setScript(script)
				.setParams(params)
				.setSortOrder(order);
	}
	public static ScriptSortDefinition script(String script,Map<String, Object> params,ScriptSortType type,SortOrder sortOrder,int order){
		return new ScriptSortDefinition()
				.setScript(script)
				.setParams(params)
				.setType(type)
				.setSortOrder(sortOrder)
				.setOrder(order);
	}
	
	
	public static GeoDistanceSortDefinition geoDistance(String field,Double latitude,Double longitude) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude);
		return sort;
	}
	public static GeoDistanceSortDefinition geoDistance(String field,Double latitude,Double longitude,SortOrder order) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setSortOrder(order);
		return sort;
	}
	public static GeoDistanceSortDefinition geoDistance(String field,Double latitude,Double longitude,DistanceUnit unit) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setUnit(unit);
		return sort;
	}
	public static GeoDistanceSortDefinition geoDistance(String field,Double latitude,Double longitude,DistanceUnit unit,SortOrder order) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setUnit(unit)
				.setSortOrder(order);
		return sort;
	}
	public static GeoDistanceSortDefinition geoDistance(String field,Double latitude,Double longitude,DistanceUnit unit,SortOrder sortOrder,int order) {
		GeoDistanceSortDefinition sort = new GeoDistanceSortDefinition()
				.setField(field)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setUnit(unit)
				.setSortOrder(sortOrder)
				.setOrder(order);
		return sort;
	}
}
