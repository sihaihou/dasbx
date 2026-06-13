package com.reyco.dasbx.es.test.condititon;

import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.reyco.dasbx.es.core.query.condition.MatchCondition;
import com.reyco.dasbx.es.core.query.condition.adapter.MatchConditionAdapter;

public class MatchConditionAdapterTest {

	private MatchConditionAdapter adapter = new MatchConditionAdapter();

	@Test
	public void testAdapt() {
		MatchCondition condition = new MatchCondition()
				.setField("name")
				.setValue("张三");

		QueryBuilder builder = adapter.adapt(condition, null, null);
		String dsl = builder.toString();

		Assert.assertTrue(dsl.contains("name"));

		Assert.assertTrue(dsl.contains("张三"));
	}
}
