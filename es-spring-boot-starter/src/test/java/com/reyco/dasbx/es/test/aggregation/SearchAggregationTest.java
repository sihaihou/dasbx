package com.reyco.dasbx.es.test.aggregation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.reyco.dasbx.es.core.feature.aggregation.Aggregations;
import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.query.condition.Querys;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.page.Pages;
import com.reyco.dasbx.es.test.TestApplication;
import com.reyco.dasbx.es.test.domain.Personage;

@ActiveProfiles("test")
@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class SearchAggregationTest {

	@Autowired
	private SearchBuilderFactory factory;

	@Test
	public void testSearch() throws Exception {
		SearchResult<Personage> result = factory.builder(Personage.class)
				.index("personage")
				.query(Querys.match("name", "侯"))
				.page(Pages.offset(1, 10))
				.aggregation(Aggregations.termsAgg("gender", "gender"))
				.search();

		Assert.assertNotNull(result);
	}
}
