package com.reyco.dasbx.es.core.sync;

import java.io.IOException;

/**
 * 增量更新ES
 * @author reyco
 *
 */
public interface IncrementUpdateSyncElasticsearchService extends SyncElasticsearchService {
	/**
	 * 增量更新
	 * @param primaryKeyId
	 * @return
	 * @throws IOException
	 */
	int incrementUpdateSyncElasticsearch(Object primaryKeyId) throws IOException;
	
	/**
	 * 增量删除
	 * @param primaryKeyId
	 * @return
	 * @throws IOException
	 */
	boolean incrementDeleteSyncElasticsearch(Object primaryKeyId) throws IOException;
}
