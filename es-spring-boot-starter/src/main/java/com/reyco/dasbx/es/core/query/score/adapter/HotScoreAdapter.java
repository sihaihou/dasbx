package com.reyco.dasbx.es.core.query.score.adapter;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

import com.reyco.dasbx.es.core.query.score.HotScoreFunction;

public class HotScoreAdapter implements FunctionAdapter<HotScoreFunction> {

	@Override
	public Class<HotScoreFunction> support() {
		return HotScoreFunction.class;
	}

	@Override
	public ScoreFunctionBuilder<?> adapt(HotScoreFunction function) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("saleWeight",function.getSaleWeight());
		params.put("commentWeight",function.getCommentWeight());
		params.put("favorWeight",function.getFavorWeight());
		
		Script script = new Script(
				ScriptType.INLINE,
				"painless",

				"_score + " +
	
					"doc['saleNum'].value * params.saleWeight + " +
	
					"doc['commentNum'].value * params.commentWeight + " +
	
					"doc['favorNum'].value * params.favorWeight",

				params);

		return ScoreFunctionBuilders.scriptFunction(script);
	}
}
