package com.reyco.dasbx.es.core.query.score;

import com.reyco.dasbx.es.core.query.score.condition.FunctionScoreCondition;

public class ScoreFunctions {

	public static FunctionScoreCondition functionScore() {
		return new FunctionScoreCondition();
	}

	public static WeightScoreFunction weight(float weight) {
		return new WeightScoreFunction(weight);
	}

	public static FieldValueFactorFunction fieldValueFactor(String field) {
		return new FieldValueFactorFunction(field);
	}

	public static ScriptScoreFunction scriptScore(String script) {
		return new ScriptScoreFunction(script);
	}

	public static RandomScoreFunction randomScore(Integer seed) {
		return new RandomScoreFunction(seed);
	}
	
}
