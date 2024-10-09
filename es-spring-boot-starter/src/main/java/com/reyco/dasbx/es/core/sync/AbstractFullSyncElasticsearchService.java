package com.reyco.dasbx.es.core.sync;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.es.core.exception.ElasticsearchException;

/**
 * 抽象全量同步
 * @author reyco
 *
 */
public abstract class AbstractFullSyncElasticsearchService implements FullSyncElasticsearchService {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractFullSyncElasticsearchService.class);

	//字符串分割表达式: '.' 或者 ',' 或者 ';' 或者  '、' 或者 '一个或多个空格' 或者 '-'
	protected final static String SPLIT_EXPRESSION = "\\.|,|，|;|；|、|\\s+|-";
	
	@Autowired
	protected ElasticsearchClient<ElasticsearchDocument> elasticsearchClient;
	
	@Override
	public int fullSyncElasticsearch() throws IOException {
		String indexName = getIndexName();
		try {
			logger.debug("【ES全量同步】全量同步开始！索引:{}",indexName);
			Long maxId = getDocumentMaxId();
			int count = 0;
			for (int i=1;i<maxId;i+=10000) {
				List<ElasticsearchDocument> elasticsearchDocuments = getListByLimit(new Long(i), new Long(1+10000));
				int batchAddDocument = elasticsearchClient.batchAddDocument(indexName, elasticsearchDocuments);
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
	 * 获取索引名称
	 * @return
	 */
	protected abstract String getIndexName();
	/**
	 * 获取索引语句
	 * @return
	 */
	protected abstract String getIndexDSL();
	/**
	 * 获取文档最大id
	 * @return
	 */
	protected abstract Long getDocumentMaxId();
	/**
	 * 获取文档列表
	 * @param startId
	 * @param endId
	 * @return
	 */
	protected abstract List<ElasticsearchDocument> getListByLimit(Long startId,Long endId);
}
