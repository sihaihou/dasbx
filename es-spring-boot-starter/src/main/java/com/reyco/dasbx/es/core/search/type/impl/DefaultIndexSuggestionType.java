package com.reyco.dasbx.es.core.search.type.impl;

import com.reyco.dasbx.es.core.search.type.IndexSuggestionType;
import com.reyco.dasbx.es.core.search.type.IndexType;

public class DefaultIndexSuggestionType implements IndexSuggestionType {
	private String indexName;
	private String suggestionName = IndexType.DEFAULT_SUGGESTION_NAME;
	private String suggestionField = IndexType.DEFAULT_SUGGESTION_FIELD;
	private int suggestionSize = IndexType.DEFAULT_SUGGESTION_SIZE;
	@Override
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	@Override
	public String getSuggestionName() {
		return suggestionName;
	}
	public void setSuggestionName(String suggestionName) {
		this.suggestionName = suggestionName;
	}
	@Override
	public String getSuggestionField() {
		return suggestionField;
	}
	public void setSuggestionField(String suggestionField) {
		this.suggestionField = suggestionField;
	}
	@Override
	public Integer getSuggestionSize() {
		return suggestionSize;
	}
	public void setSuggestionSize(int suggestionSize) {
		this.suggestionSize = suggestionSize;
	}
}
