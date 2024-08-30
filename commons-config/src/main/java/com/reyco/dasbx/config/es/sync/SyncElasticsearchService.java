package com.reyco.dasbx.config.es.sync;

import java.io.IOException;

/**
 * Elasticsearch同步工具
 * @author reyco
 *
 */
public interface SyncElasticsearchService {
	
	/**
	 * 初始化ES
	 * @return
	 * @throws IOException
	 */
	int initElasticsearch() throws IOException;
	/**
	 * 同步ES
	 * @param primaryKeyId
	 * @return
	 * @throws IOException
	 */
	int syncElasticsearch(Long primaryKeyId) throws IOException;
	
	/**
	 * 删除
	 * @param primaryKeyId
	 * @return
	 * @throws IOException
	 */
	boolean syncDeleteElasticsearch(Long primaryKeyId) throws IOException;
	
}
