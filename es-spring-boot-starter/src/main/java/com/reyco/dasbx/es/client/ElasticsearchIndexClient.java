package com.reyco.dasbx.es.client;

import java.io.IOException;
import java.util.Objects;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elasticsearch 索引和 Mapping 操作组件。
 *
 * <p>
 * 该组件只负责索引创建、Mapping 修改、索引删除和索引存在性判断， 不处理文档 CRUD 或普通搜索。
 * </p>
 */
public class ElasticsearchIndexClient {

	/**
	 * 索引组件日志。
	 */
	private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexClient.class);

	/**
	 * Elasticsearch 官方高级客户端。
	 */
	private final RestHighLevelClient client;

	/**
	 * 创建索引操作组件。
	 *
	 * @param client
	 *            Elasticsearch 官方高级客户端
	 */
	public ElasticsearchIndexClient(RestHighLevelClient client) {
		this.client = Objects.requireNonNull(client, "RestHighLevelClient must not be null");
	}

	/**
	 * 创建索引。
	 *
	 * @param indexName
	 *            索引名称
	 * @param indexDsl
	 *            创建索引使用的 JSON DSL
	 * @return 集群确认创建成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean createIndex(String indexName, String indexDsl) throws IOException {

		CreateIndexRequest request = new CreateIndexRequest(indexName);

		request.source(indexDsl, XContentType.JSON);

		CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

		boolean acknowledged = response.isAcknowledged();

		if (acknowledged) {
			log.info("Index created: {}", indexName);
		} else {
			log.error("Index creation was not acknowledged: {}", indexName);
		}

		return acknowledged;
	}

	/**
	 * 为已有索引添加或更新 Mapping。
	 *
	 * @param indexName
	 *            索引名称
	 * @param mappingDsl
	 *            Mapping JSON DSL
	 * @return 集群确认更新成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean putMapping(String indexName, String mappingDsl) throws IOException {

		PutMappingRequest request = new PutMappingRequest(indexName);

		request.source(mappingDsl, XContentType.JSON);

		AcknowledgedResponse response = client.indices().putMapping(request, RequestOptions.DEFAULT);

		boolean acknowledged = response.isAcknowledged();

		if (acknowledged) {
			log.info("Mapping updated: {}", indexName);
		} else {
			log.error("Mapping update was not acknowledged: {}", indexName);
		}

		return acknowledged;
	}

	/**
	 * 删除整个索引。
	 *
	 * <p>
	 * 该方法会删除索引结构、Mapping 和数据，调用方应确认操作范围。
	 * </p>
	 *
	 * @param indexName
	 *            索引名称
	 * @return 集群确认删除成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean deleteIndex(String indexName) throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(indexName);

		AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);

		boolean acknowledged = response.isAcknowledged();

		if (acknowledged) {
			log.info("Index deleted: {}", indexName);
		} else {
			log.error("Index deletion was not acknowledged: {}", indexName);
		}

		return acknowledged;
	}

	/**
	 * 判断索引是否存在。
	 *
	 * @param indexName
	 *            索引名称
	 * @return 索引存在时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean existsIndex(String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName);

		return client.indices().exists(request, RequestOptions.DEFAULT);
	}

}
