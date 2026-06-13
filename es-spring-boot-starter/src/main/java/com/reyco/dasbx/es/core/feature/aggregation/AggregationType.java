package com.reyco.dasbx.es.core.feature.aggregation;

public enum AggregationType {
	/**
	 * bucket
	 */
	TERMS,

	DATE_HISTOGRAM,

	RANGE,

	NESTED,

	/**
	 * metric
	 */
	AVG,

	MAX,

	MIN,

	SUM,

	COUNT
}
