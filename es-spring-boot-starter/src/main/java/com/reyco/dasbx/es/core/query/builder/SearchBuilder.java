package com.reyco.dasbx.es.core.query.builder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.highlight.HighlightDefinition;
import com.reyco.dasbx.es.core.query.builder.reactive.ReactiveSearchBuilder;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.page.PageDefinition;

public interface SearchBuilder<T> {
	/**
	 * 索引
	 */
	SearchBuilder<T> index(String index);

	/**
	 * 查询条件
	 */
	SearchBuilder<T> query(Condition condition);

	/**
	 * include source fields
	 */
	SearchBuilder<T> includeFields(String... fields);

	/**
	 * exclude source fields
	 */
	SearchBuilder<T> excludeFields(String... fields);

	/**
	 * 分页
	 */
	SearchBuilder<T> page(PageDefinition pageDefinition);
	
	/**
	 * 路由
	 * @param routing
	 * @return
	 */
	SearchBuilder<T> routing(String routing);
	
	/**
	 * 排序
	 * @param sort
	 * @return
	 */
	SearchBuilder<T> sort(SortDefinition sort);
	
	SearchBuilder<T> sorts(List<SortDefinition> sorts);

	/**
	 * 高亮
	 */
	SearchBuilder<T> highlight(String field);
	
	SearchBuilder<T> highlight(String[] fields);

	SearchBuilder<T> highlight(String field, String preTag, String postTag);

	SearchBuilder<T> highlight(HighlightDefinition highlight);
	
	SearchBuilder<T> highlight(List<HighlightDefinition> highlights);
	/**
	 * 聚合
	 * 
	 * @param aggregation
	 * @return
	 */
	SearchBuilder<T> aggregation(AggregationDefinition aggregation);
	
	SearchBuilder<T> aggregation(List<AggregationDefinition> aggregations);
	
	SearchBuilder<T> profile();

    SearchBuilder<T> explain();
	
    
    
    
	 <T> SearchBuilder<T> builder(Class<T> clazz);
	/**
	 * 搜索
	 */
	SearchResult<T> search();

	/**
	 * 异步搜索
	 */
	CompletableFuture<SearchResult<T>> searchAsync();
	
	/**
	 * 
	 */
	ReactiveSearchBuilder<T> reactive();

}
