package com.reyco.dasbx.common.core.service;

public interface EsService {
	
	/**
	 * 创建索引
	 * @param indexName
	 * @param indexDSL
	 * @return
	 */
	boolean createIndex(String indexName, String indexDSL) throws Exception;
	
	/**
	 * 添加属性
	 * @param indexName
	 * @param mapping
	 * @return
	 */
	boolean addFieldIndex(String indexName, String mapping) throws Exception;
	
	/**
	 * 索引是否存在
	 * @param indexName
	 * @return
	 */
	boolean existsIndex(String indexName) throws Exception;
	
	/**
	 * 删除索引
	 * @param indexName
	 * @return
	 */
	boolean deleteIndex(String indexName) throws Exception;

}
