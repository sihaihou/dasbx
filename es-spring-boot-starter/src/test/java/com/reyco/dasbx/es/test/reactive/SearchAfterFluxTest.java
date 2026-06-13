package com.reyco.dasbx.es.test.reactive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.query.sort.Sorts;
import com.reyco.dasbx.es.core.result.SearchHitResult;
import com.reyco.dasbx.es.core.result.page.Pages;
import com.reyco.dasbx.es.test.TestApplication;
import com.reyco.dasbx.es.test.domain.Personage;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SearchAfterFluxTest {

	@Autowired
	private SearchBuilderFactory factory;

	@Test
	public void testFlux() {
		Flux<SearchHitResult<Personage>> flux = factory.builder(Personage.class)
				.index("personage")
				.page(Pages.searchAfter(10))
				.sort(Sorts.field("id"))
				.reactive()
				.searchAfterFlux();

		StepVerifier.create(flux).expectNextCount(10).verifyComplete();
	}
}
