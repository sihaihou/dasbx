package com.reyco.dasbx.es.core.query.score.adapter;

import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import com.reyco.dasbx.es.core.query.score.WeightScoreFunction;

public class WeightFunctionAdapter implements FunctionAdapter<WeightScoreFunction> {

	@Override
	public Class<WeightScoreFunction> support() {
		return WeightScoreFunction.class;
	}

	@Override
	public ScoreFunctionBuilder<?> adapt(WeightScoreFunction function) {
		return ScoreFunctionBuilders.weightFactorFunction(function.getWeight());
	}
}