package com.reyco.dasbx.es.client;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.reyco.dasbx.es.core.query.suggest.SuggestQuery;
import com.reyco.dasbx.es.support.result.Result;
import com.reyco.dasbx.es.support.template.SearchTemplate;

/**
 * Elasticsearch 兼容门面。
 *
 * <p>
 * 保留原有公开 API，内部将索引操作委托给 {@link ElasticsearchIndexClient}，将单文档和批量文档操作委托给
 * {@link ElasticsearchDocumentClient}，搜索提示操作委托给{@link SearchTemplate}。
 * </p>
 *
 * @param <E>
 *            Elasticsearch 文档类型
 */
public class ElasticsearchClient<E extends ElasticsearchDocument> implements ApplicationContextAware {

	/**
	 * 索引和 Mapping 操作组件。
	 */
	private ElasticsearchIndexClient indexClient;

	/**
	 * 单文档和批量文档操作组件。
	 */
	private ElasticsearchDocumentClient<E> documentClient;

	/**
	 * 底层 Elasticsearch 高级客户端。
	 */
	private RestHighLevelClient restHighLevelClient;

	/**
	 * Spring 上下文，用于转发给批量失败事件发布组件。
	 */
	private ApplicationContext applicationContext;

	/**
	 * 注解式搜索与提示词服务。
	 */
	private SearchTemplate searchTemplate;

	/**
	 * 兼容旧版无参构造器。
	 */
	public ElasticsearchClient() {
	}

	/**
	 * 使用官方高级客户端创建兼容门面。
	 *
	 * @param restHighLevelClient
	 *            Elasticsearch 高级客户端
	 */
	public ElasticsearchClient(RestHighLevelClient restHighLevelClient) {
		setRestHighLevelClient(restHighLevelClient);
	}

	/**
	 * 使用官方客户端和搜索模板创建统一门面。
	 *
	 * @param restHighLevelClient
	 *            Elasticsearch 高级客户端
	 * @param searchTemplate
	 *            注解式搜索模板
	 */
	public ElasticsearchClient(
			RestHighLevelClient restHighLevelClient,
			SearchTemplate searchTemplate) {

		setRestHighLevelClient(restHighLevelClient);
		setSearchTemplate(searchTemplate);
	}

	/**
	 * 设置或替换底层客户端，并重建内部组件。
	 *
	 * @param restHighLevelClient
	 *            Elasticsearch 高级客户端
	 */
	public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {

		if (restHighLevelClient == null) {
			throw new IllegalArgumentException("RestHighLevelClient must not be null");
		}

		this.restHighLevelClient = restHighLevelClient;
		this.indexClient = new ElasticsearchIndexClient(restHighLevelClient);
		this.documentClient = new ElasticsearchDocumentClient<>(restHighLevelClient);

		if (applicationContext != null) {
			this.documentClient.setApplicationContext(applicationContext);
		}
	}

	/**
	 * 注入 Spring 上下文并转发给文档组件。
	 *
	 * @param applicationContext
	 *            Spring 上下文
	 * @throws BeansException
	 *             Spring 注入失败
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;

		if (documentClient != null) {
			documentClient.setApplicationContext(applicationContext);
		}
	}

	/**
	 * 设置搜索模板。
	 *
	 * @param searchTemplate
	 *            注解式搜索模板
	 */
	public void setSearchTemplate(SearchTemplate searchTemplate) {
		this.searchTemplate = searchTemplate;
	}

	/**
	 * 获取索引操作组件。
	 *
	 * @return 索引操作组件
	 */
	public ElasticsearchIndexClient indexClient() {
		ensureInitialized();
		return indexClient;
	}

	/**
	 * 获取文档操作组件。
	 *
	 * @return 文档操作组件
	 */
	public ElasticsearchDocumentClient<E> documentClient() {
		ensureInitialized();
		return documentClient;
	}

	/**
	 * 获取搜索模板。
	 *
	 * @return 注解式搜索模板
	 */
	public SearchTemplate searchTemplate() {
		ensureSearchInitialized();
		return searchTemplate;
	}

	/**
	 * 执行注解式搜索。
	 *
	 * @param queryDto
	 *            查询 DTO
	 * @param resultClass
	 *            返回类型
	 * @param <Q>
	 *            查询 DTO 类型
	 * @param <R>
	 *            返回类型
	 * @return 统一搜索结果
	 */
	public <Q, R> Result<R> search(Q queryDto, Class<R> resultClass) {
		ensureSearchInitialized();
		return searchTemplate.search(queryDto, resultClass);
	}

	/**
	 * 执行搜索提示。
	 *
	 * @param index
	 *            索引名称
	 * @param keyword
	 *            提示关键词
	 * @return 提示词列表
	 * @throws Exception
	 *             搜索提示执行失败
	 */
	public List<String> suggest(String index, String keyword) throws Exception {
		ensureSearchInitialized();
		return searchTemplate.suggest(index, keyword);
	}

	/**
	 * 执行指定数量的搜索提示。
	 *
	 * @param index
	 *            索引名称
	 * @param keyword
	 *            提示关键词
	 * @param size
	 *            返回数量
	 * @return 提示词列表
	 * @throws Exception
	 *             搜索提示执行失败
	 */
	public List<String> suggest(String index, String keyword, int size) throws Exception {
		ensureSearchInitialized();
		return searchTemplate.suggest(index, keyword, size);
	}

	/**
	 * 执行指定字段的搜索提示。
	 *
	 * @param index
	 *            索引名称
	 * @param field
	 *            字段名称
	 * @param keyword
	 *            提示关键词
	 * @return 提示词列表
	 * @throws Exception
	 *             搜索提示执行失败
	 */
	public List<String> suggest(String index, String field, String keyword) throws Exception {
		ensureSearchInitialized();
		return searchTemplate.suggest(index, field, keyword);
	}

	/**
	 * 执行指定字段和数量的搜索提示。
	 *
	 * @param index
	 *            索引名称
	 * @param field
	 *            字段名称
	 * @param size
	 *            返回数量
	 * @param keyword
	 *            提示关键词
	 * @return 提示词列表
	 * @throws Exception
	 *             搜索提示执行失败
	 */
	public List<String> suggest(String index, String field, int size, String keyword) throws Exception {
		ensureSearchInitialized();
		return searchTemplate.suggest(index, field, size, keyword);
	}

	/**
	 * 使用完整提示查询执行搜索提示。
	 *
	 * @param index
	 *            索引名称
	 * @param query
	 *            提示查询
	 * @return 提示词列表
	 * @throws Exception
	 *             搜索提示执行失败
	 */
	public List<String> suggest(String index, SuggestQuery query) throws Exception {
		ensureSearchInitialized();
		return searchTemplate.suggest(index, query);
	}

	/**
	 * 创建索引。
	 *
	 * @param indexName
	 *            索引名称
	 * @param indexDsl
	 *            JSON DSL
	 * @return 创建成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public Boolean createIndex(String indexName, String indexDsl) throws IOException {

		ensureInitialized();
		return indexClient.createIndex(indexName, indexDsl);
	}

	/**
	 * 添加或更新 Mapping。
	 *
	 * @param indexName
	 *            索引名称
	 * @param indexDsl
	 *            Mapping JSON DSL
	 * @return 更新成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public Boolean addFieldIndex(String indexName, String indexDsl) throws IOException {

		ensureInitialized();
		return indexClient.putMapping(indexName, indexDsl);
	}

	/**
	 * 删除整个索引。
	 *
	 * @param indexName
	 *            索引名称
	 * @return 删除成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public Boolean deleteIndex(String indexName) throws IOException {
		ensureInitialized();
		return indexClient.deleteIndex(indexName);
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
	public Boolean existsIndex(String indexName) throws IOException {
		ensureInitialized();
		return indexClient.existsIndex(indexName);
	}

	/**
	 * 判断文档是否存在。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @return 文档存在时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public Boolean existsDocument(String indexName, String id) throws IOException {

		ensureInitialized();
		return documentClient.existsDocument(indexName, id);
	}

	/**
	 * 获取文档 JSON。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @return 文档 JSON；不存在时返回 null
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public String getDocument(String indexName, String id) throws IOException {

		ensureInitialized();
		return documentClient.getDocument(indexName, id);
	}

	/**
	 * 新增文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param document
	 *            文档对象
	 * @return 写入成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public Boolean addDocument(String indexName, E document) throws IOException {

		ensureInitialized();
		return documentClient.addDocument(indexName, document);
	}

	/**
	 * 使用 ID 和 JSON 新增文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @param documentJson
	 *            文档 JSON
	 * @return 写入成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public Boolean addDocument(String indexName, String id, String documentJson) throws IOException {

		ensureInitialized();
		return documentClient.addDocument(indexName, id, documentJson);
	}

	/**
	 * 批量新增文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param documents
	 *            文档列表
	 * @return 成功处理数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public int batchAddDocument(String indexName, List<E> documents) throws IOException {

		ensureInitialized();
		return documentClient.batchAddDocument(indexName, documents);
	}

	/**
	 * 更新文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param document
	 *            文档对象
	 * @return 更新成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public Boolean updateDocument(String indexName, E document) throws IOException {

		ensureInitialized();
		return documentClient.updateDocument(indexName, document);
	}

	/**
	 * 使用 ID 和 JSON 更新文档。
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
	public Boolean updateDocument(String indexName, String id, String documentJson) throws IOException {

		ensureInitialized();
		return documentClient.updateDocument(indexName, id, documentJson);
	}

	/**
	 * 批量更新文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param documents
	 *            文档列表
	 * @return 成功处理数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public int batchUpdateDocument(String indexName, List<E> documents) throws IOException {

		ensureInitialized();
		return documentClient.batchUpdateDocument(indexName, documents);
	}

	/**
	 * 删除文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param id
	 *            文档 ID
	 * @return 文档实际删除时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public Boolean deleteDocument(String indexName, String id) throws IOException {

		ensureInitialized();
		return documentClient.deleteDocument(indexName, id);
	}

	/**
	 * 批量删除文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @param documents
	 *            文档列表
	 * @return 成功处理数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public int batchDeleteDocument(String indexName, List<E> documents) throws IOException {

		ensureInitialized();
		return documentClient.batchDeleteDocument(indexName, documents);
	}

	/**
	 * 根据文档 ID 列表批量删除。
	 *
	 * @param indexName
	 *            索引名称
	 * @param documentIds
	 *            文档 ID 列表
	 * @return 成功处理数量
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public int batchDeleteDocumentByIds(String indexName, List<String> documentIds) throws IOException {

		ensureInitialized();
		return documentClient.batchDeleteDocumentByIds(indexName, documentIds);
	}

	/**
	 * 清空索引中的全部文档。
	 *
	 * <p>
	 * 保留索引结构和 Mapping，使用 Delete By Query 删除全部文档。
	 * </p>
	 *
	 * @param indexName
	 *            索引名称
	 * @return 删除请求执行成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean clearIndex(String indexName) throws IOException {
		return deleteAllDocuments(indexName);
	}

	/**
	 * 清空索引中的全部文档。
	 *
	 * @param indexName
	 *            索引名称
	 * @return 删除请求执行成功时返回 true
	 * @throws IOException
	 *             Elasticsearch 通信失败
	 */
	public boolean deleteAllDocuments(String indexName) throws IOException {
		ensureInitialized();
		return documentClient.deleteAllDocuments(indexName);
	}

	/**
	 * 确认内部组件已经初始化。
	 */
	private void ensureInitialized() {
		if (indexClient == null || documentClient == null) {
			throw new IllegalStateException("RestHighLevelClient has not been configured");
		}
	}

	/**
	 * 确认搜索模板已经初始化。
	 */
	private void ensureSearchInitialized() {
		if (searchTemplate == null) {
			throw new IllegalStateException("SearchTemplate has not been configured");
		}
	}
}
