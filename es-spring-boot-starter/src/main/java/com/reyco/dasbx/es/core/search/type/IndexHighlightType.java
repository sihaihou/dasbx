package com.reyco.dasbx.es.core.search.type;

/**
 * 高亮类型
 * @author reyco
 *
 */
public interface IndexHighlightType {
	/**
	 * 索引模型名称
	 * @return
	 */
	String getIndexName();
	/**
	 * 高亮属性
	 * @return
	 */
	String[] getHighlightFields();
}
