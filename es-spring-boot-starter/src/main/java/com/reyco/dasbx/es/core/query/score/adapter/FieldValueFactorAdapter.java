package com.reyco.dasbx.es.core.query.score.adapter;

import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import com.reyco.dasbx.es.core.query.score.FieldValueFactorFunction;

public class FieldValueFactorAdapter implements FunctionAdapter<FieldValueFactorFunction> {

	@Override
	public Class<FieldValueFactorFunction> support() {
		return FieldValueFactorFunction.class;
	}

	@Override
	public ScoreFunctionBuilder<?> adapt(FieldValueFactorFunction function) {
		return ScoreFunctionBuilders.fieldValueFactorFunction(function.getField()).factor(function.getFactor());
	}
}
