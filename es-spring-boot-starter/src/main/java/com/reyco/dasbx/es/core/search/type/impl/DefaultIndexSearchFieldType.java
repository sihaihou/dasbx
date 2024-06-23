package com.reyco.dasbx.es.core.search.type.impl;

import com.reyco.dasbx.es.core.search.type.IndexSearchFieldType;
import com.reyco.dasbx.es.core.search.type.IndexType;

public class DefaultIndexSearchFieldType implements IndexSearchFieldType{
	private String indexName;
	private String searchField = IndexType.DEFAULT_SEARCH_FIELD;
	private String[] multiFields = IndexType.DEFAULT_SEARCH_MULTIFIELDS;
	@Override
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	@Override
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	@Override
	public String[] getMultiFields() {
		return multiFields;
	}
	public void setMultiFields(String[] multiFields) {
		this.multiFields = multiFields;
	}
}
