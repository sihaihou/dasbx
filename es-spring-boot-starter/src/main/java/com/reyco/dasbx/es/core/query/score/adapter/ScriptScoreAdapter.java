package com.reyco.dasbx.es.core.query.score.adapter;

import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

import com.reyco.dasbx.es.core.query.score.ScriptScoreFunction;

public class ScriptScoreAdapter implements FunctionAdapter<ScriptScoreFunction> {

	@Override
	public Class<ScriptScoreFunction> support() {
		return ScriptScoreFunction.class;
	}

	@Override
	public ScoreFunctionBuilder<?> adapt(ScriptScoreFunction function) {
		Script script = new Script(ScriptType.INLINE, function.getLang(), function.getScript(), function.getParams());
		return ScoreFunctionBuilders.scriptFunction(script);
	}
	
}
