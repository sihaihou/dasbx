package com.reyco.dasbx.sync.es;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.es.core.exception.ElasticsearchException;
import com.reyco.dasbx.sync.exception.SyncException;

/**
 * Elasticsearch全量同步抽象类
 * @author reyco
 *
 */
public abstract class AbstractElasticsearchFullSync<E extends ElasticsearchDocument> implements ElasticsearchFullSync<Integer>{
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractElasticsearchFullSync.class);

	protected ElasticsearchClient<ElasticsearchDocument> elasticsearchClient;
	
	public AbstractElasticsearchFullSync(ElasticsearchClient<ElasticsearchDocument> elasticsearchClient) {
		super();
		this.elasticsearchClient = elasticsearchClient;
	}
	@Override
	public Integer fullSync() throws SyncException {
		String indexName = getIndexName();
		try {
			logger.debug("【ES全量同步】全量同步开始！索引:{}",indexName);
			Long maxId = getDocumentMaxId();
			int count = 0;
			for (int i=1;i<maxId;i+=10000) {
				List<E> es = getListByLimit(new Long(i), new Long(1+10000));
				int batchAddDocument = elasticsearchClient.batchAddDocument(indexName, (List<ElasticsearchDocument>) es);
				count+=batchAddDocument;
			}
			logger.debug("【ES全量同步】全量同步完成！索引:{}",indexName);
			return count;
		} catch (Exception e) {
			handlerFullSyncException(e);
		}finally {
			logger.debug("【ES全量同步】全量同步结束！索引:{}",indexName);
		}
		return 0;
	}
	protected void handlerFullSyncException(Exception e) {
		String indexName = getIndexName();
		logger.error("【ES全量同步】全量同步失败,索引:{},异常信息:{}",indexName,e.getMessage());
		throw new ElasticsearchException(e);
	}
	/**
	 * 获取所有名称
	 * @return
	 */
	protected abstract String getIndexName();
	/**
	 * 获取索引语句
	 * 
	 * @return
	 */
	protected abstract String getIndexDSL();

	/**
	 * 获取文档最大id
	 * 
	 * @return
	 */
	protected abstract Long getDocumentMaxId();

	/**
	 * 获取文档列表
	 * @param startId
	 * @param endId
	 * @return
	 */
	protected abstract List<E> getListByLimit(Long startId, Long endId);
}
