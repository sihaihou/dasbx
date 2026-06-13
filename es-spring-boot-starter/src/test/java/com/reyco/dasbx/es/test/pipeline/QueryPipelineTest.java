package com.reyco.dasbx.es.test.pipeline;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.reyco.dasbx.es.core.pipeline.query.QueryPipeline;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.condition.Querys;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapter;
import com.reyco.dasbx.es.core.query.condition.adapter.ConditionAdapterRegistry;
import com.reyco.dasbx.es.core.query.condition.adapter.MatchConditionAdapter;

public class QueryPipelineTest {
	@Test
	public void testPipeline() {

		SearchContext context = new SearchContext(null,null,null,Querys.match("name", "张三"),null,null);

		QueryPipeline pipeline = new QueryPipeline(getConditionAdapterRegistry());

		pipeline.execute(context);

		String dsl = context.getSourceBuilder().toString();

		Assert.assertTrue(dsl.contains("张三"));
	}
	
	private ConditionAdapterRegistry getConditionAdapterRegistry(){
		ConditionAdapter<?> conditionAdapter = new MatchConditionAdapter();
		List<ConditionAdapter<?>> adapters = Arrays.asList(conditionAdapter);
		ConditionAdapterRegistry conditionAdapterRegistry = new ConditionAdapterRegistry(adapters);
		return conditionAdapterRegistry;
	}
}
