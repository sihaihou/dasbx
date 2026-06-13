package com.reyco.dasbx.es.core.feature.aggregation;

public class Aggregations {
	
	public static AggregationDefinition termsAgg(String name, String field) {
		return new AggregationDefinition()
				.setName(name)
				.setField(field)
				.setType(AggregationType.TERMS);
	}
	public static AggregationDefinition termsAgg(String name, String field, Integer size) {
		return new AggregationDefinition()
				.setName(name)
				.setField(field)
				.setSize(size)
				.setType(AggregationType.TERMS);
	}
	public static AggregationDefinition termsAgg(String name, String field, Integer size,Boolean order) {
		return new AggregationDefinition()
				.setName(name)
				.setField(field)
				.setSize(size)
				.setOrder(order)
				.setType(AggregationType.TERMS);
	}

	public static AggregationDefinition avgAgg(String name, String field) {
		return new AggregationDefinition()
				.setName(name)
				.setField(field)
				.setType(AggregationType.AVG);
	}

	public static AggregationDefinition maxAgg(String name, String field) {
		return new AggregationDefinition()
				.setName(name)
				.setField(field)
				.setType(AggregationType.MAX);
	}
	public static AggregationDefinition minAgg(String name, String field) {
		return new AggregationDefinition()
				.setName(name)
				.setField(field)
				.setType(AggregationType.MIN);
	}
	public static AggregationDefinition sum(String name, String field) {
		return new AggregationDefinition()
				.setName(name)
				.setField(field)
				.setType(AggregationType.SUM);
	}
	public static AggregationDefinition dateHistogram(String name,String field,String interval){
		return new AggregationDefinition()
				.setName(name)
				.setField(field)
				.setInterval(interval)
				.setType(AggregationType.DATE_HISTOGRAM);
	}
	
	
	public static AggregationDefinition nested(String name,String path) {
		return new AggregationDefinition()
				.setName(name)
				.setField(path)
				.setType(AggregationType.NESTED);
	}
}
