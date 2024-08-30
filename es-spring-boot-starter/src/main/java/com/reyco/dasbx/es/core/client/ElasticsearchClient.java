package com.reyco.dasbx.es.core.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse.ShardInfo;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.commons.utils.ToString;
import com.reyco.dasbx.es.core.event.ElasticSearcchErrorEvent;
import com.reyco.dasbx.es.core.event.ElasticSearcchErrorEvent.ProcessFailureMessage;
import com.reyco.dasbx.es.core.model.Aggregation;
import com.reyco.dasbx.es.core.search.type.IndexType;
import com.reyco.dasbx.es.core.utils.ElasticsearchUtils;

/**
 * es客户端
 * @author reyco
 *
 * @param <T>
 */
public class ElasticsearchClient<T extends ElasticsearchDocument> implements ApplicationContextAware {
	
	protected Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);
	
	private RestHighLevelClient restHighLevelClient;
	
	private ApplicationContext applicationContext;
	
	public ElasticsearchClient() {
	}
	public ElasticsearchClient(RestHighLevelClient restHighLevelClient) {
		super();
		this.restHighLevelClient = restHighLevelClient;
	}
	public void setRestHighLevelClient(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	/*######################################索引相关接口########################################*/
	/**
	 * 创建索引
	 * @param indexName	索引名称
	 * @param indexDSL  构建索引DSL语句
	 * @return
	 * @throws IOException
	 */
	public Boolean createIndex(String indexName,String indexDSL) throws IOException {
		try {
			CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
			createIndexRequest.source(indexDSL, XContentType.JSON);
			CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
			if(createIndexResponse.isAcknowledged()) {
				logger.info(indexName+":索引创建成功！");
			}else {
				logger.error(indexName+":索引创建失败！");
			}
			return createIndexResponse.isAcknowledged();
		} catch (Exception e) {
			logger.error(indexName+":索引创建失败,msg:"+e.getMessage());
		}
		return false;
	}
	/**
	 * 给索引添加字段
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public Boolean addFieldIndex(String indexName,String indexDSL) throws IOException {
		PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
		putMappingRequest.source(indexDSL, XContentType.JSON);
		AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
		if(acknowledgedResponse.isAcknowledged()) {
			logger.info(indexName+":添加字段成功！");
		}else {
			logger.error(indexName+":添加字段失败！");
		}
		return acknowledgedResponse.isAcknowledged();
	}
	/**
	 * 删除索引
	 * @param indexName 索引名称
	 * @return
	 * @throws IOException
	 */
	public Boolean deleteIndex(String indexName) throws IOException {
		try {
			DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
			AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteIndexRequest,RequestOptions.DEFAULT);
			if(acknowledgedResponse.isAcknowledged()) {
				logger.info(indexName+":删除索引成功！");
			}else {
				logger.error(indexName+":删除索引失败！");
			}
			return acknowledgedResponse.isAcknowledged();
		} catch (Exception e) {
			logger.error(indexName+"：删除索引失败");
		}
		return false;
	}
	/**
	 * 索引是否存在
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public Boolean existsIndex(String indexName) throws IOException {
		GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
		return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
	}
	
	
	
	/*######################################文档操作相关接口########################################*/
	/**
	 * 文档是否存在
	 * @param indexName
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public Boolean existsDocument(String indexName,String id) throws IOException {
		GetRequest getRequest = new GetRequest(indexName, id);
		return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
	}
	/**
	 * 查询文档
	 * @param indexName
	 * @param id
	 * @throws IOException
	 */
	public String getDocument(String indexName,String id) throws IOException {
		GetRequest getRequest = new GetRequest(indexName,id);
		GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
		String documentJson = getResponse.getSourceAsString();
		logger.debug("查询成功：index="+indexName+",id="+id+",document="+documentJson);
		return documentJson;
	}
	/**
	 * 新增文档
	 * @param indexName		索引名称
	 * @param document		文档对象
	 * @return
	 * @throws IOException
	 */
	public Boolean addDocument(String indexName,T t) throws IOException {
		return addDocument(indexName, t.getPrimaryKeyId(),JsonUtils.objToJson(t));
	}
	/**
	 * 新增文档
	 * @param indexName		索引名称
	 * @param id			文档id
	 * @param documentJson	文档json串
	 * @return
	 * @throws IOException
	 */
	public Boolean addDocument(String indexName,String id,String documentJson) throws IOException {
		IndexRequest indexRequest = new IndexRequest(indexName).id(id);
		indexRequest.source(documentJson, XContentType.JSON);
		IndexResponse indexResponse = restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
		ShardInfo shardInfo = indexResponse.getShardInfo();
		if(shardInfo.getSuccessful()==1 ) {
			logger.debug("添加成功：index="+indexName+",id="+id+",document="+documentJson+",indexResponse="+indexResponse);
		}else {
			logger.error("添加失败：index="+indexName+",id="+id+",document="+documentJson+",indexResponse="+indexResponse);
		}
		return shardInfo.getSuccessful()==1;
	}
	/**
	 * 批量新增文档
	 * @param indexName		索引名称
	 * @param documents		文档对象列表
	 * @return
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public int batchAddDocument(String indexName,List<T> documents) throws IOException {
		if (documents == null || documents.isEmpty()) {
			return 0;
		}
		int size = documents.size();
		int count = 5000;
		int frequency = size / count;
		int remainder = size % count;
		if (remainder > 0) {
			++frequency;
		}
		logger.debug("size="+size+",count="+count+",frequency=" + frequency);
		BulkRequest bulkRequest = null;
		IndexRequest indexRequest = null;
		List<T> tempDocuments = null;
		int success = 0;
		int fail = 0;
		for (int i = 0; i < frequency; i++) {
			if ((i + 1) == frequency) {
				tempDocuments = documents.subList(i * count, size);
			} else {
				tempDocuments = documents.subList(i * count, (i + 1) * count);
			}
			for (ElasticsearchDocument document : tempDocuments) {
				if(bulkRequest==null) {
					bulkRequest = new BulkRequest();
				}
				indexRequest = new IndexRequest(indexName).id(document.getPrimaryKeyId());
				indexRequest.source(JsonUtils.objToJson(document), XContentType.JSON);
				bulkRequest.add(indexRequest);
			}
			logger.debug("i:"+i+"添加开始");
			BulkResponse bulk = restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
			ProcessResult processResult = getProcessResult(bulk);
			success+=processResult.getSuccessQuantity();
			fail+=processResult.getFailQuantity();
			logger.debug("i:"+i+"添加结束,"+processResult.getSuccessQuantity()+"条处理成功,"+processResult.getFailQuantity()+"条处理失败,id:["+processResult.getFailPrimarykeys());
		}
		logger.debug("批量添加成功：index="+indexName+",success="+success+",fail="+fail);
		return success;
	}
	/**
	 * 更新文档 
	 * @param indexName  索引名称
	 * @param document document对象
	 * @return
	 * @throws IOException
	 */
	public Boolean updateDocument(String indexName,T t) throws IOException {
		return updateDocument(indexName,t.getPrimaryKeyId(), JsonUtils.objToJson(t));
	}
	/**
	 * 更新文档 
	 * @param indexName  索引名称
	 * @param id		 id
	 * @param documentJson 文档json字符串 
	 * @return
	 * @throws IOException
	 */
	public Boolean updateDocument(String indexName,String id,String documentJson) throws IOException {
		UpdateRequest updateRequest = new UpdateRequest(indexName, id);
		updateRequest.doc(documentJson,XContentType.JSON);
		UpdateResponse updateResponse = restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
		ShardInfo shardInfo = updateResponse.getShardInfo();
		if(shardInfo.getSuccessful()==1) {
			logger.debug("更新成功：index="+indexName+",id="+id+",document="+documentJson+",updateResponse="+updateResponse);
		}else {
			logger.error("更新失败：index="+indexName+",id="+id+",document="+documentJson+",updateResponse="+updateResponse);
		}
		return shardInfo.getSuccessful()==1;
	}
	/**
	 *  批量更新文档 
	 * @param indexName  索引名称
	 * @param id		 id
	 * @param documentJson 文档json字符串 
	 * @return
	 * @throws IOException
	 */
	public int batchUpdateDocument(String indexName,List<T> ts) throws IOException {
		if (ts == null || ts.isEmpty()) {
			return 0;
		}
		int size = ts.size();
		int count = 5000;
		int frequency = size / count;
		int remainder = size % count;
		if (remainder > 0) {
			++frequency;
		}
		logger.debug("size="+size+",count="+count+",frequency=" + frequency);
		BulkRequest bulkRequest = null;
		UpdateRequest updateRequest = null;
		List<T> tempDocuments = null;
		int success = 0;
		int fail = 0;
		for (int i = 0; i < frequency; i++) {
			if ((i + 1) == frequency) {
				tempDocuments = ts.subList(i * count, size);
			} else {
				tempDocuments = ts.subList(i * count, (i + 1) * count);
			}
			for (T t : tempDocuments) {
				if(bulkRequest==null) {
					bulkRequest = new BulkRequest();
				}
				updateRequest = new UpdateRequest(indexName,t.getPrimaryKeyId());
				updateRequest.doc(JsonUtils.objToJson(t), XContentType.JSON);
				bulkRequest.add(updateRequest);
			}
			logger.debug("i:"+i+"更新开始");
			BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
			ProcessResult processResult = getProcessResult(bulk);
			success+=processResult.getSuccessQuantity();
			fail+=processResult.getFailQuantity();
			logger.debug("i:"+i+"更新结束,"+processResult.getSuccessQuantity()+"条处理成功,"+processResult.getFailQuantity()+"条处理失败,id:["+processResult.getFailPrimarykeys());
		}
		logger.debug("批量更新成功：index="+indexName+",success="+success+",fail="+fail);
		return success;
	}
	/**
	 * 删除文档
	 * @param indexName		索引名称
	 * @param id			文档id
	 * @return
	 * @throws IOException
	 */
	public Boolean deleteDocument(String indexName,String id) throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(indexName,id);
		DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
		ShardInfo shardInfo = deleteResponse.getShardInfo();
		if(shardInfo.getSuccessful()==1) {
			logger.debug("删除成功：index="+indexName+",id="+id+",deleteResponse="+deleteResponse);
		}else {
			logger.error("删除失败：index="+indexName+",id="+id+",deleteResponse="+deleteResponse);
		}
		return shardInfo.getSuccessful()==1;
	}
	/**
	 * 批量删除文档
	 * @param indexName		索引名称
	 * @param documents     文档对象列表
	 * @return
	 * @throws IOException
	 */
	public int batchDeleteDocument(String indexName,List<T> ts) throws IOException {
		List<String> documentIds = null;
		for (T t : ts) {
			if(documentIds==null) {
				documentIds = new ArrayList<>();
			}
			documentIds.add(t.getPrimaryKeyId());
		}
		return batchDeleteDocumentByIds(indexName, documentIds);
	}
	/**
	 * 批量删除文档
	 * @param indexName		索引名称
	 * @param documentIds	文档ids
	 * @return 
	 * @throws IOException
	 */
	public int batchDeleteDocumentByIds(String indexName,List<String> documentIds) throws IOException {
		if (documentIds == null || documentIds.isEmpty()) {
			return 0;
		}
		int size = documentIds.size();
		int count = 10000;
		int frequency = size / count;
		int remainder = size % count;
		if (remainder > 0) {
			++frequency;
		}
		logger.info("size="+size+",count="+count+",frequency=" + frequency);
		BulkRequest bulkRequest = null;
		DeleteRequest deleteRequest = null;
		List<String> tempDocumentIds = null;
		int success = 0;
		int fail = 0;
		for (int i = 0; i < frequency; i++) {
			if ((i + 1) == frequency) {
				tempDocumentIds = documentIds.subList(i * count, size);
			} else {
				tempDocumentIds = documentIds.subList(i * count, (i + 1) * count);
			}
			for(String documentId : tempDocumentIds) {
				if(bulkRequest==null) {
					bulkRequest = new BulkRequest();
				}
				deleteRequest = new DeleteRequest(indexName,documentId);
				bulkRequest.add(deleteRequest);
			}
			logger.debug("i:"+i+"删除开始");
			BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
			ProcessResult processResult = getProcessResult(bulk);
			success+=processResult.getSuccessQuantity();
			fail+=processResult.getFailQuantity();
			logger.debug("i:"+i+"删除结束,"+processResult.getSuccessQuantity()+"条处理成功,"+processResult.getFailQuantity()+"条处理失败,id:["+processResult.getFailPrimarykeys());
		}
		logger.debug("批量删除成功：index="+indexName+",success="+success+",fail="+fail);
		return success;
	}
	/**
	 * 清空索引下数据
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public boolean clearIndex(String indexName) throws IOException {
		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
		AcknowledgedResponse response = restHighLevelClient.indices().delete(deleteIndexRequest,RequestOptions.DEFAULT);
		return response.isAcknowledged();
	}
	
	
	
	
	
	
	
	
	/*######################################自动补全相关接口########################################*/
	/**
	 * 获取自动补全
	 * @param indexName				索引
	 * @param keyword				搜索关键字
	 * @return
	 * @throws IOException
	 */
	public List<String> getSuggestion(String indexName,String keyword) throws IOException{
		return getSuggestion(indexName, keyword, IndexType.DEFAULT_SUGGESTION_FIELD,IndexType.DEFAULT_SUGGESTION_SIZE);
	}
	/**
	 * 获取自动补全
	 * @param indexName				索引
	 * @param keyword				搜索关键字
	 * @param completionFieldName   completion类型的字段
	 * @return
	 * @throws IOException
	 */
	public List<String> getSuggestion(String indexName,String keyword,String completionFieldName) throws IOException{
		return getSuggestion(indexName, keyword, completionFieldName,IndexType.DEFAULT_SUGGESTION_SIZE);
	}
	/**
	 * 获取自动补全
	 * @param indexName				索引
	 * @param keyword				搜索关键字
	 * @param size   				查询记录数
	 * @return
	 * @throws IOException
	 */
	public List<String> getSuggestion(String indexName,String keyword,Integer size) throws IOException{
		return getSuggestion(indexName, keyword, IndexType.DEFAULT_SUGGESTION_FIELD,size);
	}
	/**
	 * 获取自动补全
	 * @param indexName				索引
	 * @param keyword				搜索关键字
	 * @param completionFieldName   completion类型的字段
	 * @param size   				查询记录数
	 * @return
	 * @throws IOException
	 */
	public List<String> getSuggestion(String indexName,String keyword,String completionFieldName,Integer size) throws IOException{
		if(size==null || size<0) {
			size = IndexType.DEFAULT_SUGGESTION_SIZE;
		}
		String suggestionName = completionFieldName;
		logger.info("获取自动补全：index="+indexName+",keyword="+keyword+",suggestionName="+suggestionName+",size="+size);
		SearchRequest searchRequest = new SearchRequest(indexName);
		ElasticsearchUtils.buildSuggestion(searchRequest,keyword,completionFieldName,size);
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
		return ElasticsearchUtils.parseSuggest(searchResponse.getSuggest(), suggestionName);
	}
	/**
	 * 获取自动补全
	 * @param indexName				索引
	 * @param keyword				搜索关键字
	 * @param completionFieldName   completion类型的字段
	 * @param suggestionName   		completion类型的名称
	 * @param size   				查询记录数
	 * @return
	 * @throws IOException
	 */
	public List<String> getSuggestion(String indexName,String keyword,String completionFieldName,String suggestionName,Integer size) throws IOException{
		logger.info("获取自动补全：index="+indexName+",keyword="+keyword+",completionFieldName="+completionFieldName+",suggestionName="+suggestionName+",size="+size);
		SearchRequest searchRequest = new SearchRequest(indexName);
		ElasticsearchUtils.buildSuggestion(searchRequest,keyword,completionFieldName,suggestionName,size);
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
		return ElasticsearchUtils.parseSuggest(searchResponse.getSuggest(), suggestionName);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*######################################获取类目聚合数据相关接口########################################*/
	/*类目聚合查询时：聚合时添加查询相关的过滤条件*/
	/**
	 * 获取聚合数据
	 * @param searchRequest
	 * @param aggregationFields
	 * @return
	 * @throws IOException
	 */
	public Map<String, List<Aggregation>> getAggsCategory(SearchRequest searchRequest,String[] aggregationFields) throws IOException {
		return getAggsCategory(searchRequest, IndexType.DEFAULT_AGGREGATION_PAGE_MIN_SIZE, aggregationFields, null, null, null);
	}
	/**
	 * 获取聚合数据
	 * @param searchRequest
	 * @param size
	 * @param aggregationFields
	 * @return
	 * @throws IOException
	 */
	public Map<String, List<Aggregation>> getAggsCategory(SearchRequest searchRequest,Integer size,String[] aggregationFields) throws IOException {
		return getAggsCategory(searchRequest, size, aggregationFields, null, null, null);
	}
	/**
	 * 获取聚合数据
	 * @param searchRequest
	 * @param size
	 * @param aggregationFields
	 * @param aggregationOrders
	 * @return
	 * @throws IOException
	 */
	public Map<String, List<Aggregation>> getAggsCategory(SearchRequest searchRequest,Integer size,String[] aggregationFields,Boolean[] aggregationOrders) throws IOException {
		return getAggsCategory(searchRequest, size, aggregationFields, null, aggregationOrders, null);
	}
	/**
	 * 获取聚合数据
	 * @param searchRequest
	 * @param size
	 * @param aggregationFields
	 * @param aggregationOrders
	 * @param aggregationSizes
	 * @return
	 * @throws IOException
	 */
	public Map<String, List<Aggregation>> getAggsCategory(SearchRequest searchRequest,Integer size,String[] aggregationFields,Boolean[] aggregationOrders,Integer[] aggregationSizes) throws IOException {
		return getAggsCategory(searchRequest, size, aggregationFields, null, aggregationOrders, aggregationSizes);
	}
	/**
	 * 获取聚合数据
	 * @param searchRequest
	 * @param size
	 * @param aggregationFields
	 * @param aggregationNames
	 * @param aggregationOrders
	 * @return
	 * @throws IOException
	 */
	public Map<String, List<Aggregation>> getAggsCategory(SearchRequest searchRequest,Integer size,String[] aggregationFields,String[] aggregationNames,Boolean[] aggregationOrders) throws IOException {
		return getAggsCategory(searchRequest, size, aggregationFields, aggregationNames, aggregationOrders, null);
	}
	/**
	 * 获取聚合数据
	 * @param searchRequest
	 * @param size
	 * @param aggregationFields
	 * @param aggregationNames
	 * @param aggregationOrders
	 * @param aggregationSizes
	 * @return
	 * @throws IOException
	 */
	public Map<String, List<Aggregation>> getAggsCategory(SearchRequest searchRequest,Integer size,String[] aggregationFields,String[] aggregationNames,Boolean[] aggregationOrders,Integer[] aggregationSizes) throws IOException {
		if(size==null || size<IndexType.DEFAULT_AGGREGATION_PAGE_MIN_SIZE) {
			size = IndexType.DEFAULT_AGGREGATION_PAGE_MIN_SIZE;
		}
		if(size>IndexType.DEFAULT_AGGREGATION_PAGE_MAX_SIZE) {
			size = IndexType.DEFAULT_AGGREGATION_PAGE_MAX_SIZE;
		}
		searchRequest.source().size(size);
		ElasticsearchUtils.buildAggregation(searchRequest,aggregationFields,aggregationNames,aggregationOrders,aggregationSizes);
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
		return ElasticsearchUtils.parseAggregations(searchResponse.getAggregations(), aggregationNames);
	}
	
	/**
	 * 获取RestHighLevelClient客户端
	 * @return
	 */
	public RestHighLevelClient restHighLevelClient() {
		return restHighLevelClient;
	}
	
	
	
	
	/**
	 * 获取处理结果
	 * @param bulkResponse
	 * @return
	 */
	private ProcessResult getProcessResult(BulkResponse bulkResponse) {
		BulkItemResponse[] items = bulkResponse.getItems();
		Integer total = items.length;
		List<String> successPrimarykeys = new ArrayList<>();
		List<String> failPrimarykeys = new ArrayList<>();
		if(bulkResponse.hasFailures()) {
			List<ProcessFailureMessage> processFailureMessages = new ArrayList<>();
			for(BulkItemResponse bulkItemResponse : items) {
				if(bulkItemResponse.isFailed()) {
					ProcessFailureMessage parseFailureMessage = parseFailureMessage(bulkItemResponse);
					processFailureMessages.add(parseFailureMessage);
					failPrimarykeys.add(bulkItemResponse.getId());
				}else {
					successPrimarykeys.add(bulkItemResponse.getId());
				}
			}
			publishFailureMessage(processFailureMessages);
		}
		ProcessResult processResult = new ProcessResult();
		processResult.setSuccessQuantity(total-failPrimarykeys.size());
		processResult.setSuccessPrimarykeys(successPrimarykeys);
		processResult.setFailQuantity(failPrimarykeys.size());
		processResult.setFailPrimarykeys(failPrimarykeys);
		return processResult;
	}
	/**
	 * 解析失败信息
	 * @param bulkItemResponse
	 * @return
	 */
	private ProcessFailureMessage parseFailureMessage(BulkItemResponse bulkItemResponse) {
		ProcessFailureMessage processFailureMessage = null;
		if(bulkItemResponse.isFailed()) {
			String index = bulkItemResponse.getIndex();
			String id = bulkItemResponse.getId();
			String failureMessage = bulkItemResponse.getFailureMessage();
			String type = bulkItemResponse.getOpType().name();
			processFailureMessage = new ProcessFailureMessage();
			processFailureMessage.setIndex(index);
			processFailureMessage.setType(type);
			processFailureMessage.setPrimarykey(id);
			processFailureMessage.setFailureMessage(failureMessage);
		}
		return processFailureMessage;
	}
	/**
	 * 发布失败信息
	 * @param processFailureMessages
	 */
	private void publishFailureMessage(List<ProcessFailureMessage> processFailureMessages) {
		if(!CollectionUtils.isEmpty(processFailureMessages)) {
			ElasticSearcchErrorEvent errorEvent = new ElasticSearcchErrorEvent(this, processFailureMessages);
			applicationContext.publishEvent(errorEvent);
		}
	}
	/**
	 * 处理结果
	 * @author reyco
	 *
	 */
	public static class ProcessResult extends ToString{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7849344423058511696L;
		private Integer successQuantity;
		private List<String> successPrimarykeys;
		private Integer failQuantity;
		private List<String> failPrimarykeys;
		public Integer getSuccessQuantity() {
			return successQuantity;
		}
		public void setSuccessQuantity(Integer successQuantity) {
			this.successQuantity = successQuantity;
		}
		public List<String> getSuccessPrimarykeys() {
			return successPrimarykeys;
		}
		public void setSuccessPrimarykeys(List<String> successPrimarykeys) {
			this.successPrimarykeys = successPrimarykeys;
		}
		public Integer getFailQuantity() {
			return failQuantity;
		}
		public void setFailQuantity(Integer failQuantity) {
			this.failQuantity = failQuantity;
		}
		public List<String> getFailPrimarykeys() {
			return failPrimarykeys;
		}
		public void setFailPrimarykeys(List<String> failPrimarykeys) {
			this.failPrimarykeys = failPrimarykeys;
		}
	}
}
