package com.reyco.dasbx.es.test.sort;

import org.elasticsearch.search.sort.SortOrder;
import org.junit.Assert;
import org.junit.Test;

import com.reyco.dasbx.es.core.metadata.register.DefaultMetadataRegistry;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.sort.FieldSortDefinition;
import com.reyco.dasbx.es.core.query.sort.strategy.FieldSortStrategy;

public class FieldSortStrategyTest {
	
	private FieldSortStrategy strategy = new FieldSortStrategy(new DefaultMetadataRegistry());

	@Test
	public void testSort() {
		
		SearchContext context = new SearchContext(null,null,null,null,null,null);

		FieldSortDefinition sort = new FieldSortDefinition()
				.setField("name")
				.setSortOrder(SortOrder.DESC);

		strategy.apply(sort, context);

		String dsl = context.getSourceBuilder().toString();

		Assert.assertTrue(dsl.contains("name"));
	}
}
