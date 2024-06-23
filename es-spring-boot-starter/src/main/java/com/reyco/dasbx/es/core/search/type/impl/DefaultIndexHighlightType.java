package com.reyco.dasbx.es.core.search.type.impl;

import com.reyco.dasbx.es.core.search.type.IndexHighlightType;

public class DefaultIndexHighlightType implements IndexHighlightType{
	private String indexName;
	private String[] highlightFields;
	@Override
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	@Override
	public String[] getHighlightFields() {
		return highlightFields;
	}
	public void setHighlightFields(String[] highlightFields) {
		this.highlightFields = highlightFields;
	}
}
