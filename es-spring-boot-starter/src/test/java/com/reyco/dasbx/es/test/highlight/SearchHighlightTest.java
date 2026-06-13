package com.reyco.dasbx.es.test.highlight;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.query.condition.Querys;
import com.reyco.dasbx.es.core.result.SearchHitResult;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.page.Pages;
import com.reyco.dasbx.es.test.TestApplication;
import com.reyco.dasbx.es.test.domain.Personage;

@ActiveProfiles("test")
@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class SearchHighlightTest {

	@Autowired
	private SearchBuilderFactory factory;

	@Test
	public void testSearch() throws Exception {
		SearchResult<Personage> result = factory.builder(Personage.class)
				.index("personage")
				.query(Querys.multiMatch("PO00000003", "name","code","masterpiece"))
				.page(Pages.offset(1, 10))
				.highlight("name")
				.highlight("code")
				.highlight("masterpiece")
				.search();

		Assert.assertNotNull(result);
		for (SearchHitResult<Personage> searchHitResult : result.getRecords()) {
			Map<String, List<String>> highlights = searchHitResult.getHighlights();
			for(Entry<String, List<String>>  entity : highlights.entrySet()) {
				System.out.println("key:"+entity.getKey()+",value:"+entity.getValue());
			}
		}
	}
}
