package com.reyco.dasbx.sync.es;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.es.core.exception.ElasticsearchException;
import com.reyco.dasbx.sync.exception.SyncException;

/**
 * 抽象同步
 * 
 * @author reyco
 *
 */
public abstract class AbstractElasticsearchSync<T,E extends ElasticsearchDocument> extends AbstractElasticsearchFullSync<E> implements ElasticsearchSync<T,Integer> {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractElasticsearchFullSync.class);

	//字符串分割表达式: '.' 或者 ',' 或者 ';' 或者  '、' 或者 '一个或多个空格' 或者 '-'
	public final static String SPLIT_EXPRESSION = "\\.|,|，|;|；|、|\\s+|-";
	
	public AbstractElasticsearchSync(ElasticsearchClient<ElasticsearchDocument> elasticsearchClient) {
		super(elasticsearchClient);
	}
	@Override
	public Integer incrementUpdateSync(Supplier<T> supplier) throws SyncException {
		T primaryKeyId = supplier.get();
		if (primaryKeyId == null || (primaryKeyId instanceof String && StringUtils.isBlank((String) primaryKeyId))) {
			return 0;
		}
		String indexName = getIndexName();
		try {
			logger.debug("【ES增量更新】增量更新开始！索引:{},主键:{}", indexName, primaryKeyId);
			E e = getElasticsearchDocumentByPrimaryKeyId(supplier);
			if (e != null) {
				Boolean exists = elasticsearchClient.existsDocument(indexName, e.getPrimaryKeyId());
				boolean success = exists ? elasticsearchClient.updateDocument(indexName, e)
						: elasticsearchClient.addDocument(indexName, e);
				if(success) {
					logger.debug("【ES增量更新】增量更新完成！索引:{},主键:{}", indexName, primaryKeyId);
				}else {
					logger.warn("【ES增量更新】增量更新或添加操作失败！索引:{},主键:{}", indexName, primaryKeyId);
				}
				return success ? 1 : 0;
			} else {
				logger.warn("【ES增量更新】未找到需要更新的文档！索引:{},主键:{}", indexName, primaryKeyId);
				return 0;
			}
		} catch (Exception e) {
			handlerIncrementUpdateException(e, primaryKeyId);
		} finally {
			logger.debug("【ES增量更新】增量更新结束！索引:{},主键:{}", indexName, primaryKeyId);
		}
		return 0;
	}

	protected void handlerIncrementUpdateException(Exception e, T primaryKeyId) {
		String indexName = getIndexName();
		logger.error("【ES增量更新】增量更新失败,索引:{},异常信息:{},主键:{}", indexName, e.getMessage(), primaryKeyId);
		throw new ElasticsearchException(e);
	}

	@Override
	public Integer incrementDeleteSync(Supplier<T> supplier) throws SyncException {
		T primaryKeyId = supplier.get();
		if (primaryKeyId == null || (primaryKeyId instanceof String && StringUtils.isBlank((String) primaryKeyId))) {
			return 0;
		}
		String primaryKey = null;
		if (primaryKeyId instanceof String) {
			primaryKey = (String) primaryKeyId;
		} else {
			primaryKey = String.valueOf(primaryKeyId); // 或者抛出异常，取决于你的业务逻辑
		}
		String indexName = getIndexName();
		try {
			logger.debug("【ES增量删除】增量删除开始！索引:{},主键:{}", indexName);
			Boolean success = elasticsearchClient.deleteDocument(getIndexName(), primaryKey);
			if (success) {
				logger.debug("【ES增量删除】增量删除成功！索引:{},主键:{}", indexName, primaryKeyId);
			} else {
				logger.warn("【ES增量删除】增量删除操作完成，但可能未删除任何文档！索引:{},主键:{}", indexName, primaryKeyId);
			}
			return success ? 1 : 0;
		} catch (Exception e) {
			handlerIncrementDeleteException(e, primaryKeyId);
		} finally {
			logger.debug("【ES增量删除】增量删除结束！索引:{},主键:{}", indexName, primaryKey);
		}
		return 0;
	}
	protected void handlerIncrementDeleteException(Exception e, T primaryKey) {
		String indexName = getIndexName();
		logger.error("【ES增量删除】增量删除失败,索引:{},异常信息:{},主键:{}", indexName, e.getMessage(), primaryKey);
		throw new ElasticsearchException(e);
	}
	/**
	 * 获取ElasticsearchDocument对象
	 * 
	 * @param supplier
	 * @return
	 */
	protected abstract E getElasticsearchDocumentByPrimaryKeyId(Supplier<T> supplier);

}
