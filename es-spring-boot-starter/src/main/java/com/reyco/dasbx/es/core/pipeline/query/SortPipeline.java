package com.reyco.dasbx.es.core.pipeline.query;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.sort.SortOrder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.sort.ScoreSortDefinition;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.query.sort.Sorts;
import com.reyco.dasbx.es.core.query.sort.strategy.SortStrategy;
import com.reyco.dasbx.es.core.query.sort.strategy.SortStrategyRegistry;

public class SortPipeline implements SearchPipeline {

	private SortStrategyRegistry registry;

	public SortPipeline(SortStrategyRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void execute(SearchContext context) {
		try {
			SearchQuery query = context.getQuery();
			if (query == null) {
				return;
			}
			
			List<SortDefinition> sorts = query.getSorts();
			if(sorts==null) {
				sorts = new ArrayList<>();
				query.setSorts(sorts);
			}
			
			// 1. 是否设置了score排序
			boolean hasScore = sorts.stream().anyMatch(sort -> sort instanceof ScoreSortDefinition);
			// 2. 兜底策略：如果没有传 Score 排序，我们主动实例化一个塞进去
	        if (!hasScore) {
	            // 假设你的子类或父类可以设置排序方向和框架优先级Order
	            sorts.add(Sorts.score(SortOrder.DESC, Ordered.HIGHEST_PRECEDENCE));
	        }
	        
			//先排序
			AnnotationAwareOrderComparator.sort(sorts);
			//
			for (SortDefinition sort : sorts) {
				SortStrategy<SortDefinition> strategy = (SortStrategy<SortDefinition>) registry.get(sort.getClass());
				strategy.apply(sort, context);
			}
		}catch (Exception e) {
			throw new SearchBuildException("Sort build error!",e);
		}
	}
	@Override
	public int getOrder() {
		return 40;
	}
}
