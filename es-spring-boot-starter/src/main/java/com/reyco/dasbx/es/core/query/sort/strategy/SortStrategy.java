package com.reyco.dasbx.es.core.query.sort.strategy;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;

public interface SortStrategy<T extends SortDefinition>{
	 /**
     * 排序类型
     */
    Class<T> support();
    /**
     * 构建排序
     */
	void apply(T sort,SearchContext context);
	
}