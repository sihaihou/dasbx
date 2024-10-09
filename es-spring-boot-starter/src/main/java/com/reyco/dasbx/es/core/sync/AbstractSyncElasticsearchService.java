package com.reyco.dasbx.es.core.sync;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.es.core.exception.ElasticsearchException;

/**
 * 抽象ES同步
 * @author reyco
 *
 */
public abstract class AbstractSyncElasticsearchService extends AbstractFullSyncElasticsearchService implements IncrementUpdateSyncElasticsearchService{
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractSyncElasticsearchService.class);
	
	@Override
	public int incrementUpdateSyncElasticsearch(Object primaryKeyId) throws IOException {
		if(primaryKeyId == null) {
			return 0;
		}
		if(primaryKeyId instanceof String) {
			String primaryKeyIdStr = (String)primaryKeyId;
			if(StringUtils.isBlank(primaryKeyIdStr)) {
				return 0;
			}
		}
		String indexName = getIndexName();
		try {
			logger.debug("【ES增量更新】增量更新开始！索引:{},主键:{}",indexName,primaryKeyId);
			Boolean flag = false;
			ElasticsearchDocument elasticsearchDocument = getElasticsearchDocumentByPrimaryKeyId(primaryKeyId);
			if(elasticsearchDocument != null) {
				if(elasticsearchClient.existsDocument(indexName, elasticsearchDocument.getPrimaryKeyId())) {
					flag = elasticsearchClient.updateDocument(indexName, elasticsearchDocument);
				}else {
					flag = elasticsearchClient.addDocument(indexName, elasticsearchDocument);
				}
			}
			logger.debug("【ES增量更新】增量更新完成！索引:{},主键:{}",indexName,primaryKeyId);
			return flag ? 1 : 0;
		} catch (Exception e) {
			handlerIncrementUpdateException(e,primaryKeyId);
		}finally {
			logger.debug("【ES增量更新】增量更新结束！索引:{},主键:{}",indexName,primaryKeyId);
		}
		return 0;
	}
	protected void handlerIncrementUpdateException(Exception e,Object primaryKeyId) {
		String indexName = getIndexName();
		logger.error("【ES增量更新】增量更新失败,索引:{},异常信息:{},主键:{}",indexName,e.getMessage(),primaryKeyId);
		throw new ElasticsearchException(e);
	}
	
	
	
	@Override
	public boolean incrementDeleteSyncElasticsearch(Object primaryKeyId) throws IOException {
		if(primaryKeyId == null) {
			return false;
		}
		if(primaryKeyId instanceof String) {
			String primaryKeyIdStr = (String)primaryKeyId;
			if(StringUtils.isBlank(primaryKeyIdStr)) {
				return false;
			}
		}
		String indexName = getIndexName();
		try {
			logger.debug("【ES增量删除】增量删除开始！索引:{},主键:{}",indexName);
			Boolean flag = false;
			ElasticsearchDocument elasticsearchDocument = getElasticsearchDocumentByPrimaryKeyId(primaryKeyId);
			if(elasticsearchDocument!=null) {
				if(elasticsearchClient.existsDocument(getIndexName(), elasticsearchDocument.getPrimaryKeyId())) {
					flag = elasticsearchClient.deleteDocument(getIndexName(), elasticsearchDocument.getPrimaryKeyId());
				}
			}
			logger.debug("【ES增量删除】增量删除完成！索引:{},主键:{}",indexName);
			return flag;
		} catch (Exception e) {
			handlerIncrementDeleteException(e,primaryKeyId);
		}finally {
			logger.debug("【ES增量删除】增量删除结束！索引:{},主键:{}",indexName);
		}
		return false;
	}
	protected void handlerIncrementDeleteException(Exception e,Object primaryKeyId) {
		String indexName = getIndexName();
		logger.error("【ES增量删除】增量删除失败,索引:{},异常信息:{},主键:{}",indexName,e.getMessage(),primaryKeyId);
		throw new ElasticsearchException(e);
	}
	
	
	/**
	 * 获取ElasticsearchDocument对象
	 * @param primaryKeyId
	 * @return
	 */
	protected abstract ElasticsearchDocument getElasticsearchDocumentByPrimaryKeyId(Object primaryKeyId);
	
}
