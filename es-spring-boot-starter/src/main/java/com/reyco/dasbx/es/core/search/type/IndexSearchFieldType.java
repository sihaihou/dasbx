package com.reyco.dasbx.es.core.search.type;

/**
 * 搜索类型
 * @author reyco
 *
 */
public interface IndexSearchFieldType {
	/**
	 * 索引名称
	 * @return
	 */
	String getIndexName();
	/**
	 * 索引单个字段搜索: all
	 * @return
	 */
	 default String getSearchField() {
		 return IndexType.DEFAULT_SEARCH_FIELD;
	 }
	/**
	 * 索引多个字段搜索
	 * @return
	 */
	 default String[] getMultiFields() {
		 return IndexType.DEFAULT_SEARCH_MULTIFIELDS;
	 }
}
