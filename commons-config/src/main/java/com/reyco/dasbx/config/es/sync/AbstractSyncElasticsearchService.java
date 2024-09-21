package com.reyco.dasbx.config.es.sync;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.dasbx.config.redis.RedisUtil;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.model.constants.CachePrefixConstants;

public abstract class AbstractSyncElasticsearchService implements SyncElasticsearchService {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractSyncElasticsearchService.class);
	
	//字符串分割表达式: '.' 或者 ',' 或者 ';' 或者  '、' 或者 '一个或多个空格' 或者 '-'
	protected final static String SPLIT_EXPRESSION = "\\.|,|，|;|；|、|\\s+|-";
	
	@Autowired
	private ElasticsearchClient<ElasticsearchDocument> elasticsearchClient;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public int initElasticsearch() throws IOException {
		String indexName = getIndexName();
		String key = CachePrefixConstants.SYNC_ES_INIT_PREFIX+indexName;
		try {
			if(redisUtil.hasKey(key)) {
				return 0;
			}
			redisUtil.set(key, "true");
			/*elasticsearchClient.clearIndex(indexName);
			if(elasticsearchClient.existsIndex(getIndexName())) {
				elasticsearchClient.deleteIndex(getIndexName());
			}
			if(!elasticsearchClient.createIndex(getIndexName(), getIndexDSL())) {
				return 0;
			}*/
			Long maxId = getDocumentMaxId();
			int count = 0;
			for (int i=1;i<maxId;i+=10000) {
				List<ElasticsearchDocument> elasticsearchDocuments = getListByLimit(new Long(i), new Long(1+10000));
				int batchAddDocument = elasticsearchClient.batchAddDocument(indexName, elasticsearchDocuments);
				count+=batchAddDocument;
			}
			return count;
		} catch (Exception e) {
			handlerInitException(e);
		}finally {
			redisUtil.delete(key);
		}
		return 0;
	}
	protected void handlerInitException(Exception e) {
		logger.error("【ES初始化】初始化失败,出现异常,异常信息:{}",e.getMessage());
		e.printStackTrace();
	}
	@Override
	public int syncElasticsearch(Long primaryKeyId) throws IOException {
		ElasticsearchDocument elasticsearchDocument = getElasticsearchDocumentByPrimaryKeyId(primaryKeyId);
		Boolean flag = false;
		if(elasticsearchClient.existsDocument(getIndexName(), elasticsearchDocument.getPrimaryKeyId())) {
			flag = elasticsearchClient.updateDocument(getIndexName(), elasticsearchDocument);
		}else {
			flag = elasticsearchClient.addDocument(getIndexName(), elasticsearchDocument);
		}
		return flag ? 1 : 0;
	}
	
	@Override
	public boolean syncDeleteElasticsearch(Long primaryKeyId) throws IOException {
		if(elasticsearchClient.existsDocument(getIndexName(), primaryKeyId.toString())) {
			return elasticsearchClient.deleteDocument(getIndexName(), primaryKeyId.toString());
		}
		return true;
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
	/**
	 * 获取ElasticsearchDocument对象
	 * @param primaryKeyId
	 * @return
	 */
	protected abstract ElasticsearchDocument getElasticsearchDocumentByPrimaryKeyId(Long primaryKeyId);
}
