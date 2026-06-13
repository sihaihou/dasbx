package com.reyco.dasbx.es.core.pipeline.query;

import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.pipeline.SearchPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.optimize.OptimizeRule;
import com.reyco.dasbx.es.core.query.optimize.OptimizeRuleRegistry;

public class QueryOptimizePipeline implements SearchPipeline {

	private OptimizeRuleRegistry registry;

	public QueryOptimizePipeline(OptimizeRuleRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void execute(SearchContext context) {
		try {
			SearchQuery query = context.getQuery();
			if (query == null) {
				return;
			}
	
			Condition root = query.getCondition();
			if (root == null) {
				return;
			}
			for (OptimizeRule optimizer : registry.getOptimizeRules()) {
				optimizer.apply(root);
			}
		}catch (Exception e) {
			throw new SearchBuildException("QueryOptimize build error!",e);
		}
	}

	/**
	 * rewrite后 build前
	 */
	@Override
	public int getOrder() {
		return 15;
	}
}
