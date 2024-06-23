package com.reyco.dasbx.es.core.client;

/** 
 * ES的Document对象
 * @author  reyco
 * @date    2022.01.10
 * @version v1.0.1 
 */
public interface ElasticsearchDocument{
	/**
	 * es文档的id
	 * @return
	 */
	String getPrimaryKeyId();
	
}
