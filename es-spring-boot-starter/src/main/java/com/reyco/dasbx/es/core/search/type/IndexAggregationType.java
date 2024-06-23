package com.reyco.dasbx.es.core.search.type;

/**
 * 聚合
 * @author reyco
 *
 */
public interface IndexAggregationType {
	/**
	 * 索引名称
	 * @return
	 */
	String getIndexName();
	/**
	 * 聚合字段
	 * @return
	 */
	String[] getAggregationFields();
	/**
	 * 聚合名称
	 * @return
	 */
	String[] getAggregationNames();
	/**
	 * 聚合大小
	 * @return
	 */
	Integer[] getAggregationSizes();
	/**
	 * 聚合排序
	 * @return
	 */
	Boolean[] getAggregationFieldOrders();
	
}
