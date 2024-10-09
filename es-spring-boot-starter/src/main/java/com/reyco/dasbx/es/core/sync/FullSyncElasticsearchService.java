package com.reyco.dasbx.es.core.sync;

import java.io.IOException;

/**
 * 全量同步ES
 * @author reyco
 *
 */
public interface FullSyncElasticsearchService extends SyncElasticsearchService {

	/**
	 * 全量同步
	 * @return
	 * @throws IOException
	 */
	int fullSyncElasticsearch() throws IOException;

}
