package com.reyco.dasbx.es.core.pipeline.query;

import java.util.Collection;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.rewirte.QueryRewriteRegistry;

public class QueryRewritePipeline implements SearchPipeline {

	private QueryRewriteRegistry registry;

	public QueryRewritePipeline(QueryRewriteRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void execute(SearchContext context) {
		try {
			SearchQuery query = context.getQuery();
	
			// 排序
			rewrite(query.getSorts(), context);
			// 聚合
			rewrite(query.getAggregations(), context);
		}catch (Exception e) {
			throw new SearchBuildException("QueryRewrite build error!",e);
		}
	}

	/**
	 * 重写
	 * 
	 * @param definitions
	 * @param context
	 */
	private void rewrite(Collection<?> definitions, SearchContext context) {
		if (definitions == null) {
			return;
		}
		for (Object definition : definitions) {
			registry.rewrite(definition, context);
		}
	}

	@Override
	public int getOrder() {
		return 10;
	}
}