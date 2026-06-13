package com.reyco.dasbx.es.core.query.builder.reactive;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.reyco.dasbx.es.core.engine.SearchEngine;
import com.reyco.dasbx.es.core.explain.SearchExplain;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.result.SearchHitResult;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.mapper.ResultMapper;
import com.reyco.dasbx.es.core.result.page.PageDefinition;
import com.reyco.dasbx.es.core.result.page.SearchAfterPageDefinition;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DefaultReactiveSearchBuilder<T> implements ReactiveSearchBuilder<T> {
	/**
	 * 返回类型
	 */
	private Class<T> clazz;
	/**
	 * 索引
	 */
	private String index;
	/**
	 * 查询对象
	 */
	private SearchQuery query = new SearchQuery();

	/**
	 * 查询条件
	 */
	private Condition condition;
	/**
	 * 搜索引擎
	 */
	private SearchEngine engine;

	/**
	 * 结果映射
	 */
	private ResultMapper mapper;
	
	private SearchProfile profile;

	private SearchExplain explain;
	
	public DefaultReactiveSearchBuilder(Class<T> clazz, SearchEngine engine, ResultMapper mapper, String index,
			SearchQuery query, Condition condition,SearchProfile profile,SearchExplain explain) {
		this.clazz = clazz;
		this.engine = engine;
		this.mapper = mapper;
		this.index = index;
		this.query = query;
		this.condition = condition;
		
		this.profile = profile;
		this.explain = explain;
	}

	@Override
	public Mono<SearchResult<T>> mono() {
		SearchContext context = new SearchContext(clazz,index, query, condition, profile, explain);
		return Mono.fromFuture(engine.executeAsync(context)).map(response -> {
			try {
				return mapper.map(context, response, clazz);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Override
	public Flux<SearchHitResult<T>> flux() {
		return mono().flatMapMany(result -> {
			if (result == null || result.getRecords() == null) {
				return Flux.empty();
			}
			return Flux.fromIterable(result.getRecords());
		});
	}

	@Override
	public Flux<SearchHitResult<T>> searchAfterFlux() {
		PageDefinition pageDefinition = query.getPage();
		if (!(pageDefinition instanceof SearchAfterPageDefinition)) {
			return Flux.error(new IllegalArgumentException("page must be SearchAfterPageDefinition"));
		}

		SearchAfterPageDefinition page = (SearchAfterPageDefinition) pageDefinition;
		// search_after必须有sort
		if (query.getSorts() == null || query.getSorts().isEmpty()) {
			return Flux.error(new IllegalArgumentException("search_after requires sort"));
		}

		return Flux.create(sink -> {
			Object[] searchAfter = null;
			try {
				while (true) {
					if (searchAfter != null) {
						page.setSearchAfter(searchAfter);
					}
					SearchContext context = new SearchContext(clazz,index, query, condition, profile, explain);
					SearchResponse response = engine.execute(context);
					SearchResult<T> result = mapper.map(context, response, clazz);
					List<SearchHitResult<T>> records = result.getRecords();
					if (records == null || records.isEmpty()) {
						sink.complete();
						return;
					}

					for (SearchHitResult<T> record : records) {
						if (record != null) {
							sink.next(record);
						}
					}

					SearchHit[] hits = response.getHits().getHits();
					if (hits == null || hits.length == 0) {
						sink.complete();
						return;
					}

					SearchHit lastHit = hits[hits.length - 1];
					searchAfter = lastHit.getSortValues();
					if (searchAfter == null) {
						sink.complete();
						return;
					}
				}

			} catch (Exception e) {
				sink.error(e);
			}
		});
	}
}
