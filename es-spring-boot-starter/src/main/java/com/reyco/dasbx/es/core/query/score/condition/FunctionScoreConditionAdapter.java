package com.reyco.dasbx.es.core.query.score.condition;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapterRegistry;
import com.reyco.dasbx.es.core.query.score.ScoreFunction;
import com.reyco.dasbx.es.core.query.score.adapter.FunctionAdapterRegistry;

public class FunctionScoreConditionAdapter implements ConditionAdapter<FunctionScoreCondition> {

	private FunctionAdapterRegistry registry;

	public FunctionScoreConditionAdapter(FunctionAdapterRegistry registry) {
		this.registry = registry;
	}

	@Override
	public Class<FunctionScoreCondition> support() {
		return FunctionScoreCondition.class;
	}

	@Override
	public QueryBuilder adapt(FunctionScoreCondition condition,SearchContext searchContext, ConditionAdapterRegistry conditionAdapterRegistry) {

		QueryBuilder queryBuilder = conditionAdapterRegistry.adapt(condition.getQuery(), searchContext);

		List<FilterFunctionBuilder> builders = new ArrayList<>();
		for (ScoreFunction function : condition.getFunctions()) {
			builders.add(new FilterFunctionBuilder(registry.adapt(function)));
		}

		FunctionScoreQueryBuilder builder = QueryBuilders.functionScoreQuery(queryBuilder,builders.toArray(new FilterFunctionBuilder[0]));
		builder.boostMode(condition.getBoostMode());

		return builder;
	}
}
