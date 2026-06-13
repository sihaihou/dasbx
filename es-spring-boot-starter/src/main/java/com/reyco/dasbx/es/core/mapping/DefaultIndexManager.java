package com.reyco.dasbx.es.core.mapping;

import java.util.Map;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.metadata.IndexMetadata;

public class DefaultIndexManager implements IndexManager {

	private RestHighLevelClient client;

	private MappingBuilder mappingBuilder;

	public DefaultIndexManager(RestHighLevelClient client, MappingBuilder mappingBuilder) {
		this.client = client;
		this.mappingBuilder = mappingBuilder;
	}

	@Override
	public void createIfNotExists(String index, IndexMetadata metadata) {
		try {
			// 1.检测是否存在
			GetIndexRequest get = new GetIndexRequest(index);
			boolean exists = client.indices().exists(get, RequestOptions.DEFAULT);
			if (exists) {
				return;
			}
			// 2.创建索引
			CreateIndexRequest request = new CreateIndexRequest(index);
			Map<String, Object> mapping = mappingBuilder.build(metadata);
			request.mapping(mapping);
			client.indices().create(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			throw new SearchBuildException("Create index failed", e);
		}
	}
}
