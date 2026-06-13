package com.reyco.dasbx.es.test.reactive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.query.condition.Querys;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.test.TestApplication;
import com.reyco.dasbx.es.test.domain.Personage;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ReactiveSearchTest {

	@Autowired
	private SearchBuilderFactory factory;

	@Test
	public void testMono() {
		Mono<SearchResult<Personage>> mono = factory.builder(Personage.class)
				.index("personage")
				.query(Querys.match("name", "侯"))
				.reactive()
				.mono();
		StepVerifier.create(mono).expectNextCount(1).verifyComplete();
	}
}
