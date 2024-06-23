package com.reyco.dasbx.es.core.search.type.impl;

import com.reyco.dasbx.es.core.search.type.IndexAggregationType;
import com.reyco.dasbx.es.core.search.type.IndexHighlightType;
import com.reyco.dasbx.es.core.search.type.IndexSearchFieldType;
import com.reyco.dasbx.es.core.search.type.IndexSuggestionType;
import com.reyco.dasbx.es.core.search.type.IndexType;

public class DefaultIndexType implements IndexType {
	private String indexName;
	private IndexSearchFieldType indexSearchFieldType;
	private IndexHighlightType indexHighlightType;
	private IndexSuggestionType indexSuggestionType;
	private IndexAggregationType indexAggregationType;
	private int pageSize = IndexType.DEFAULT_PAGE_SIZE;
	@Override
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	@Override
	public IndexSearchFieldType getIndexSearchFieldType() {
		return indexSearchFieldType;
	}
	public void setIndexSearchFieldType(IndexSearchFieldType indexSearchFieldType) {
		this.indexSearchFieldType = indexSearchFieldType;
	}
	@Override
	public IndexHighlightType getIndexHighlightType() {
		return indexHighlightType;
	}
	public void setIndexHighlightType(IndexHighlightType indexHighlightType) {
		this.indexHighlightType = indexHighlightType;
	}
	@Override
	public IndexSuggestionType getIndexSuggestionType() {
		return indexSuggestionType;
	}
	public void setIndexSuggestionType(IndexSuggestionType indexSuggestionType) {
		this.indexSuggestionType = indexSuggestionType;
	}
	@Override
	public IndexAggregationType getIndexAggregationType() {
		return indexAggregationType;
	}
	public void setIndexAggregationType(IndexAggregationType indexAggregationType) {
		this.indexAggregationType = indexAggregationType;
	}
	@Override
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
