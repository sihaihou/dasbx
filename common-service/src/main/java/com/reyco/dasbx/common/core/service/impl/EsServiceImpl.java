package com.reyco.dasbx.common.core.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.service.EsService;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;

@Service
public class EsServiceImpl implements EsService{
	
	@Autowired
	private ElasticsearchClient<?> elasticsearchDocumentUtils;

	@Override
	public boolean createIndex(String indexName, String indexDSL) throws IOException {
		return elasticsearchDocumentUtils.createIndex(indexName, indexDSL);
	}

	@Override
	public boolean addFieldIndex(String indexName, String mapping) throws IOException {
		return elasticsearchDocumentUtils.addFieldIndex(indexName, mapping);
	}

	@Override
	public boolean existsIndex(String indexName) throws IOException {
		return elasticsearchDocumentUtils.existsIndex(indexName);
	}
	@Override
	public boolean deleteIndex(String indexName) throws IOException {
		return elasticsearchDocumentUtils.deleteIndex(indexName);
	}

}
