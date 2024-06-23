package com.reyco.dasbx.es.core.search.type;

/**
 * 自动补全类型
 * @author reyco
 *
 */
public interface IndexSuggestionType {
	/**
	 * 索引名称
	 * @return
	 */
	String getIndexName();
	/**
	 * 自动补全名称
	 * @return
	 */
	default String getSuggestionName() {
		return IndexType.DEFAULT_SUGGESTION_NAME;
	}
	/**
	 * 自动补全字段
	 * @return
	 */
	default String getSuggestionField() {
		return IndexType.DEFAULT_SUGGESTION_FIELD;
	}
	/**
	 * 字段补全大小
	 * @return
	 */
	default Integer getSuggestionSize() {
		return IndexType.DEFAULT_SUGGESTION_SIZE;
	}
}
