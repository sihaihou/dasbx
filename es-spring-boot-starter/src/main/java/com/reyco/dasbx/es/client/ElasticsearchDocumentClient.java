package com.reyco.dasbx.es.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse.ShardInfo;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.commons.utils.serializable.ToString;
import com.reyco.dasbx.es.client.ElasticSearcchErrorEvent.ProcessFailureMessage;

/**
 * Elasticsearch 单文档和批量文档操作组件。
 *
 * <p>
 * 该组件只负责文档读取、写入、更新、删除以及批量操作， 不负责索引创建、Mapping 修改和普通搜索。索引操作由
 * {@link ElasticsearchIndexClient} 负责。
 * </p>
 *
 * @param <E>
 *            Elasticsearch 文档类型
 */
public class ElasticsearchDocumentClient<E extends ElasticsearchDocument> implements ApplicationContextAware {

	/**
	 * 文档组件日志。
	 */
	private static final Logger log = LoggerFactory.getLogger(ElasticsearchDocumentClient.class);

	/**
	 * 单文档新增和更新时的默认批量分片大小。
	 */
	private static final int DOCUMENT_BATCH_SIZE = 5000;

	/**
	 * 批量删除时的默认批量分片大小。
	 */
	private static final int DELETE_BATCH_SIZE = 10000;

	/**
	 * Elasticsearch 官方高级客户端。
	 */
	private final RestHighLevelClient client;

	/**
	 * 用于发布批量操作失败事件。
	 */
	private ApplicationContext applicationContext;

	/**
	 * 创建文档操作组件。
	 *
	 * @param client
	 *            Elasticsearch 官方高级客户端
	 */
	public ElasticsearchDocumentClient(RestHighLevelClient client) {
		this.client = Objects.requireNonNull(client, "RestHighLevelClient must not be null");
	}

	/**
	 * 注入 Spring 上下文，用于发布批量处理失败事件。
	 *
	 * @param applicationContext
	 *            Spring 上下文
	 * @throws BeansException
	 *             Spring 注入失败
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 判断指定索引中的文档是否存在。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @return 文档存在时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean existsDocument(String indexName, String id) throws IOException {

		GetRequest request = new GetRequest(indexName, id);

		return client.exists(request, RequestOptions.DEFAULT);
	}

	/**
	 * 根据索引和文档 ID 获取文档 JSON。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @return 文档 JSON；文档不存在时返回 null
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public String getDocument(String indexName, String id) throws IOException {

		GetRequest request = new GetRequest(indexName, id);
		GetResponse response = client.get(request, RequestOptions.DEFAULT);

		if (!response.isExists()) {
			log.debug("Document not found: index={}, id={}", indexName, id);
			return null;
		}

		String documentJson = response.getSourceAsString();

		log.debug("Document loaded: index={}, id={}", indexName, id);

		return documentJson;
	}

	/**
	 * 新增文档。
	 *
	 * <p>
	 * 文档 ID 使用 {@link ElasticsearchDocument#getPrimaryKeyId()}。
	 * </p>
	 *
	 * @param indexName
	 *            索引名称
	 * @param document
	 *            文档对象
	 * @return 写入成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean addDocument(String indexName, E document) throws IOException {

		Objects.requireNonNull(document, "ElasticsearchDocument must not be null");

		return addDocument(indexName, document.getPrimaryKeyId(), JsonUtils.objToJson(document));
	}

	/**
	 * 使用文档 ID 和 JSON 新增文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @param documentJson
	 *            文档 JSON
	 * @return 全部目标分片写入成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean addDocument(String indexName, String id, String documentJson) throws IOException {

		IndexRequest request = new IndexRequest(indexName).id(id);

		request.source(documentJson, XContentType.JSON);

		IndexResponse response = client.index(request, RequestOptions.DEFAULT);

		boolean successful = isWriteSuccessful(response.getShardInfo());

		if (successful) {
			log.debug("Document indexed: index={}, id={}, result={}", indexName, id, response.getResult());
		} else {
			log.error("Document index failed: index={}, id={}, shards={}", indexName, id, response.getShardInfo());
		}

		return successful;
	}

	/**
	 * 使用文档对象更新文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param document
	 *            文档对象
	 * @return 更新成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean updateDocument(String indexName, E document) throws IOException {

		Objects.requireNonNull(document, "ElasticsearchDocument must not be null");

		return updateDocument(indexName, document.getPrimaryKeyId(), JsonUtils.objToJson(document));
	}

	/**
	 * 使用文档 ID 和 JSON 更新文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @param documentJson
	 *            文档 JSON
	 * @return 更新成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean updateDocument(String indexName, String id, String documentJson) throws IOException {

		UpdateRequest request = new UpdateRequest(indexName, id);

		request.doc(documentJson, XContentType.JSON);

		UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

		boolean successful = isWriteSuccessful(response.getShardInfo());

		if (successful) {
			log.debug("Document updated: index={}, id={}, result={}", indexName, id, response.getResult());
		} else {
			log.error("Document update failed: index={}, id={}, shards={}", indexName, id, response.getShardInfo());
		}

		return successful;
	}

	/**
	 * 根据文档 ID 删除文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @return 文档实际删除时返回 true；文档不存在时返回 false
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean deleteDocument(String indexName, String id) throws IOException {

		DeleteRequest request = new DeleteRequest(indexName, id);

		DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

		if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
			log.debug("Document not found for delete: index={}, id={}", indexName, id);
			return false;
		}

		boolean successful = isWriteSuccessful(response.getShardInfo());

		if (successful) {
			log.debug("Document deleted: index={}, id={}", indexName, id);
		} else {
			log.error("Document delete failed: index={}, id={}, shards={}", indexName, id, response.getShardInfo());
		}

		return successful;
	}

	/**
	 * 批量新增文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param documents
	 *            文档列表
	 * @return 成功处理的文档数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public int batchAddDocument(String indexName, List<E> documents) throws IOException {

		if (documents == null || documents.isEmpty()) {
			return 0;
		}

		return executeBulk("index", documents, DOCUMENT_BATCH_SIZE, (bulkRequest, document) -> {
			Objects.requireNonNull(document, "ElasticsearchDocument must not be null");

			IndexRequest request = new IndexRequest(indexName).id(document.getPrimaryKeyId());

			request.source(JsonUtils.objToJson(document), XContentType.JSON);

			bulkRequest.add(request);
		});
	}

	/**
	 * 批量更新文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param documents
	 *            文档列表
	 * @return 成功处理的文档数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public int batchUpdateDocument(String indexName, List<E> documents) throws IOException {

		if (documents == null || documents.isEmpty()) {
			return 0;
		}

		return executeBulk("update", documents, DOCUMENT_BATCH_SIZE, (bulkRequest, document) -> {
			Objects.requireNonNull(document, "ElasticsearchDocument must not be null");

			UpdateRequest request = new UpdateRequest(indexName, document.getPrimaryKeyId());

			request.doc(JsonUtils.objToJson(document), XContentType.JSON);

			bulkRequest.add(request);
		});
	}

	/**
	 * 批量删除文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param documents
	 *            文档列表
	 * @return 成功处理的文档数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public int batchDeleteDocument(String indexName, List<E> documents) throws IOException {

		if (documents == null || documents.isEmpty()) {
			return 0;
		}

		List<String> ids = new ArrayList<>(documents.size());

		for (E document : documents) {
			Objects.requireNonNull(document, "ElasticsearchDocument must not be null");

			ids.add(document.getPrimaryKeyId());
		}

		return batchDeleteDocumentByIds(indexName, ids);
	}

	/**
	 * 根据文档 ID 列表批量删除文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param documentIds
	 *            文档 ID 列表
	 * @return 成功处理的文档数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public int batchDeleteDocumentByIds(String indexName, List<String> documentIds) throws IOException {

		if (documentIds == null || documentIds.isEmpty()) {
			return 0;
		}

		return executeBulk("delete", documentIds, DELETE_BATCH_SIZE, (bulkRequest, documentId) -> {
			Objects.requireNonNull(documentId, "Document id must not be null");

			bulkRequest.add(new DeleteRequest(indexName, documentId));
		});
	}

	/**
	 * 清空索引中的全部文档，保留索引结构和 Mapping。
	 *
	 * <p>
	 * 该方法使用 Delete By Query 删除全部文档，并等待刷新完成。
	 * </p>
	 *
	 * @param indexName
	 *            索引名称
	 * @return 删除请求执行成功且没有超时、批量失败或搜索失败时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean deleteAllDocuments(String indexName) throws IOException {
		DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);

		request.setQuery(QueryBuilders.matchAllQuery());
		request.setRefresh(true);

		BulkByScrollResponse response = client.deleteByQuery(request, RequestOptions.DEFAULT);

		boolean successful = !response.isTimedOut() && response.getBulkFailures().isEmpty()
				&& response.getSearchFailures().isEmpty();

		if (successful) {
			log.info("All documents deleted: index={}, deleted={}", indexName, response.getDeleted());
		} else {
			log.error("Delete all documents failed: index={}, " + "timedOut={}, bulkFailures={}, searchFailures={}",
					indexName, response.isTimedOut(), response.getBulkFailures(), response.getSearchFailures());
		}

		return successful;
	}

	/**
	 * 按固定大小对集合分块执行 Bulk 请求。
	 *
	 * <p>
	 * 每个分块都会重新创建 BulkRequest，防止前一批操作被重复发送。
	 * </p>
	 *
	 * @param operation
	 *            操作名称，用于日志
	 * @param items
	 *            待处理元素
	 * @param chunkSize
	 *            每个 Bulk 请求的最大元素数量
	 * @param actionBuilder
	 *            将单个元素转换为 Bulk 操作
	 * @param <T>
	 *            元素类型
	 * @return 成功处理数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	private <T> int executeBulk(String operation, List<T> items, int chunkSize,
			BiConsumer<BulkRequest, T> actionBuilder) throws IOException {

		int success = 0;
		int failure = 0;

		for (int start = 0; start < items.size(); start += chunkSize) {
			int end = Math.min(start + chunkSize, items.size());
			List<T> chunk = items.subList(start, end);

			BulkRequest bulkRequest = new BulkRequest();

			for (T item : chunk) {
				actionBuilder.accept(bulkRequest, item);
			}

			BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);

			BulkResult result = processBulkResponse(response);

			success += result.getSuccessQuantity();
			failure += result.getFailQuantity();

			log.debug("Bulk {} chunk completed: start={}, end={}, " + "success={}, failure={}, failedIds={}", operation,
					start, end, result.getSuccessQuantity(), result.getFailQuantity(), result.getFailPrimaryKeys());
		}

		log.debug("Bulk {} completed: total={}, success={}, failure={}", operation, items.size(), success, failure);

		return success;
	}

	/**
	 * 解析单个 Bulk 请求的响应。
	 *
	 * <p>
	 * 无论 Bulk 是否存在失败，都会收集成功和失败 ID。
	 * </p>
	 *
	 * @param response
	 *            Bulk 响应
	 * @return 解析后的处理结果
	 */
	private BulkResult processBulkResponse(BulkResponse response) {
		List<String> successIds = new ArrayList<>();
		List<String> failedIds = new ArrayList<>();
		List<ProcessFailureMessage> failures = new ArrayList<>();

		for (BulkItemResponse item : response.getItems()) {
			if (item.isFailed()) {
				failedIds.add(item.getId());
				failures.add(toFailureMessage(item));
			} else {
				successIds.add(item.getId());
			}
		}

		publishFailures(failures);

		BulkResult result = new BulkResult();
		result.setSuccessQuantity(successIds.size());
		result.setSuccessPrimaryKeys(successIds);
		result.setFailQuantity(failedIds.size());
		result.setFailPrimaryKeys(failedIds);

		return result;
	}

	/**
	 * 将 Bulk 失败项转换为错误事件消息。
	 *
	 * @param response
	 *            Bulk 单项响应
	 * @return 失败消息
	 */
	private ProcessFailureMessage toFailureMessage(BulkItemResponse response) {

		ProcessFailureMessage message = new ProcessFailureMessage();

		message.setIndex(response.getIndex());
		message.setType(response.getOpType().name());
		message.setPrimarykey(response.getId());
		message.setFailureMessage(response.getFailureMessage());

		return message;
	}

	/**
	 * 发布批量操作失败事件。
	 *
	 * @param failures
	 *            失败消息
	 */
	private void publishFailures(List<ProcessFailureMessage> failures) {

		if (failures.isEmpty() || applicationContext == null) {
			return;
		}

		applicationContext.publishEvent(new ElasticSearcchErrorEvent(this, failures));
	}

	/**
	 * 判断写入是否成功。
	 *
	 * <p>
	 * 不能使用 successful == 1 判断，因为索引可能存在多个分片。
	 * </p>
	 *
	 * @param shardInfo
	 *            分片响应
	 * @return 至少一个分片成功且没有失败分片时返回 true
	 */
	private boolean isWriteSuccessful(ShardInfo shardInfo) {
		return shardInfo != null && shardInfo.getSuccessful() > 0 && shardInfo.getFailed() == 0;
	}


	/**
	 * Bulk 操作结果。
	 */
	public static class BulkResult extends ToString {

		private static final long serialVersionUID = 7849344423058511696L;

		/**
		 * 成功处理数量。
		 */
		private Integer successQuantity;

		/**
		 * 成功处理的文档 ID。
		 */
		private List<String> successPrimaryKeys;

		/**
		 * 失败处理数量。
		 */
		private Integer failQuantity;

		/**
		 * 失败处理的文档 ID。
		 */
		private List<String> failPrimaryKeys;

		public Integer getSuccessQuantity() {
			return successQuantity;
		}

		public void setSuccessQuantity(Integer successQuantity) {
			this.successQuantity = successQuantity;
		}

		public List<String> getSuccessPrimaryKeys() {
			return successPrimaryKeys;
		}

		public void setSuccessPrimaryKeys(List<String> successPrimaryKeys) {
			this.successPrimaryKeys = successPrimaryKeys;
		}

		public Integer getFailQuantity() {
			return failQuantity;
		}

		public void setFailQuantity(Integer failQuantity) {
			this.failQuantity = failQuantity;
		}

		public List<String> getFailPrimaryKeys() {
			return failPrimaryKeys;
		}

		public void setFailPrimaryKeys(List<String> failPrimaryKeys) {
			this.failPrimaryKeys = failPrimaryKeys;
		}
	}
}
