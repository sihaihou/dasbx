package com.reyco.dasbx.es.core.search.type;

/**
 * 索引类型
 * @author reyco
 *
 */
public interface IndexType {
	/**
	 * 默认总的数量
	 */
	public final static Integer DEFAULT_TOTAL_SIZE = 10000;
	/**
	 * 默认分页大小
	 */
	public final static Integer DEFAULT_PAGE_SIZE = 10;
	/**
	 * 默认分页最小值
	 */
	public final static Integer DEFAULT_MIN_PAGE_SIZE = 1;
	/**
	 * 默认分页最大值
	 */
	public final static Integer DEFAULT_MAX_PAGE_SIZE = 1000;
	/**
	 * 默认页码最大值
	 */
	public final static Integer DEFAULT_MAX_PAGE_NUM = 1000;
	
	/**
	 * 默认查询字段
	 */
	public final static String DEFAULT_SEARCH_FIELD = "all";
	/**
	 * 默认查询字段
	 */
	public final static String[] DEFAULT_SEARCH_MULTIFIELDS = {"name","remark"};
	
	/**
	 * 默认高亮前缀标签
	 */
	public final static String DEFAULT_HIGHLIGHT_PREFIX_TAG = "<em>";
	/**
	 * 默认高亮后缀标签
	 */
	public final static String DEFAULT_HIGHLIGHT_SUFFIX_TAG = "</em>";
	
	
	/**
	 * 默认自动补全查询数量
	 */
	public final static Integer DEFAULT_SUGGESTION_SIZE = 10;
	/**
	 * 默认字段补全最小数量
	 */
	public final static Integer DEFAULT_SUGGESTION_MIN_SIZE = 1;
	/**
	 * 默认字段补全最大数量
	 */
	public final static Integer DEFAULT_SUGGESTION_MAX_SIZE = 1000;
	/**
	 * 默认自动补全属性
	 */
	public final static String DEFAULT_SUGGESTION_FIELD = "suggestion";
	/**
	 * 默认自动补全属性
	 */
	public final static String DEFAULT_SUGGESTION_NAME = "suggestion";
	
	
	/**
	 * 聚合分页最小值
	 */
	public final static Integer DEFAULT_AGGREGATION_PAGE_MIN_SIZE = 10;
	/**
	 * 聚合分页最大值
	 */
	public final static Integer DEFAULT_AGGREGATION_PAGE_MAX_SIZE = 1000;
	/**
	 * 默认聚合排序大小
	 */
	public final static Integer DEFAULT_AGGREGATION_SIZE = 10;
	/**
	 * 默认聚合排序方式
	 */
	public final static Boolean DEFAULT_AGGREGATION_ORDER = true;
	/**
	 * 索引名称
	 * @return
	 */
	String getIndexName();
	
	/**
	 * 索引查询字段类型
	 * @return
	 */
	IndexSearchFieldType getIndexSearchFieldType();
	/**
	 * 索引高亮类型
	 * @return
	 */
	IndexHighlightType getIndexHighlightType();
	/**
	 * 索引自动提示类型
	 * @return
	 */
	IndexSuggestionType getIndexSuggestionType();
	/**
	 * 索引聚合类型
	 * @return
	 */
	IndexAggregationType getIndexAggregationType();
	/**
	 * 分页大小
	 * @return
	 */
	int getPageSize();
}
