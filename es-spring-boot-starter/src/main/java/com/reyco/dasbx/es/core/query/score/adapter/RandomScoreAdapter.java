package com.reyco.dasbx.es.core.query.score.adapter;

import org.elasticsearch.index.query.functionscore.RandomScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import com.reyco.dasbx.es.core.query.score.RandomScoreFunction;

public class RandomScoreAdapter implements FunctionAdapter<RandomScoreFunction> {

	@Override
	public Class<RandomScoreFunction> support() {
		return RandomScoreFunction.class;
	}

	@Override
	public ScoreFunctionBuilder<?> adapt(RandomScoreFunction function) {
		RandomScoreFunctionBuilder builder = ScoreFunctionBuilders.randomFunction();
		if (function.getSeed() != null) {
			builder.seed(function.getSeed());
		}
		return builder;
	}

}
