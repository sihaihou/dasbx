package com.reyco.dasbx.es.core.query.score.condition;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.lucene.search.function.CombineFunction;

import com.reyco.dasbx.es.core.query.condition.AbstractCondition;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.score.ScoreFunction;

public class FunctionScoreCondition extends AbstractCondition {
	/**
	 * 原查询
	 */
	private Condition query;
	/**
	 * functions
	 */
	private List<ScoreFunction> functions = new ArrayList<>();
	/**
	 * boost mode
	 */
	private CombineFunction boostMode = CombineFunction.SUM;

	public FunctionScoreCondition query(Condition query) {
		this.query = query;
		return this;
	}

	public FunctionScoreCondition addFunction(ScoreFunction function) {
		this.functions.add(function);
		return this;
	}

	public FunctionScoreCondition boostMode(CombineFunction boostMode) {
		this.boostMode = boostMode;
		return this;
	}

	public Condition getQuery() {
		return query;
	}

	public List<ScoreFunction> getFunctions() {
		return functions;
	}

	public CombineFunction getBoostMode() {
		return boostMode;
	}
}
