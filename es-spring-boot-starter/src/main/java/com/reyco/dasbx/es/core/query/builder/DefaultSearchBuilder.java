package com.reyco.dasbx.es.core.query.builder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.elasticsearch.action.search.SearchResponse;

import com.reyco.dasbx.es.core.engine.SearchEngine;
import com.reyco.dasbx.es.core.exception.SearchMappingException;
import com.reyco.dasbx.es.core.explain.SearchExplain;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.highlight.HighlightDefinition;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.builder.reactive.DefaultReactiveSearchBuilder;
import com.reyco.dasbx.es.core.query.builder.reactive.ReactiveSearchBuilder;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.mapper.ResultMapper;
import com.reyco.dasbx.es.core.result.page.PageDefinition;

public class DefaultSearchBuilder<T> implements SearchBuilder<T> {
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

	public DefaultSearchBuilder(Class<T> clazz, SearchEngine engine, ResultMapper mapper) {
		this.clazz = clazz;
		this.engine = engine;
		this.mapper = mapper;
	}

	@Override
	public SearchBuilder<T> index(String index) {
		this.index = index;
		return this;
	}

	@Override
	public SearchBuilder<T> query(Condition condition) {
		this.condition = condition;
		return this;
	}

	@Override
	public SearchBuilder<T> includeFields(String... fields) {
		query.setIncludeFields(Arrays.asList(fields));
		return this;
	}

	@Override
	public SearchBuilder<T> excludeFields(String... fields) {
		query.setExcludeFields(Arrays.asList(fields));
		return this;
	}

	@Override
	public SearchBuilder<T> page(PageDefinition page) {
		query.setPage(page);
		return this;
	}

	@Override
	public SearchBuilder<T> routing(String routing) {
		query.setRouting(routing);
		return this;
	}

	@Override
	public SearchBuilder<T> sort(SortDefinition sort) {
		if (sort != null) {
			query.getSorts().add(sort);
		}
		return this;
	}

	@Override
	public SearchBuilder<T> sorts(List<SortDefinition> sorts) {
		if (sorts != null && sorts.size()>0) {
			for (SortDefinition sort : sorts) {
				query.getSorts().add(sort);
			}
		}
		return this;
	}

	@Override
	public SearchBuilder<T> highlight(String field) {
		query.addHighlight(new HighlightDefinition(field));
		return this;
	}
	@Override
	public SearchBuilder<T> highlight(String[] fields) {
		if(fields!=null && fields.length>0) {
			for (String field : fields) {
				query.addHighlight(new HighlightDefinition(field));
			}
		}
		return this;
	}
	@Override
	public SearchBuilder<T> highlight(String field, String preTag, String postTag) {
		query.addHighlight(new HighlightDefinition(field, preTag, postTag));
		return this;
	}

	@Override
	public SearchBuilder<T> highlight(HighlightDefinition highlight) {
		query.addHighlight(highlight);
		return this;
	}
	@Override
	public SearchBuilder<T> highlight(List<HighlightDefinition> highlights) {
		if(highlights!=null && highlights.size()>0) {
			for (HighlightDefinition highlight : highlights) {
				query.addHighlight(highlight);
			}
		}
		return this;
	}
	
	@Override
	public SearchBuilder<T> aggregation(AggregationDefinition aggregation) {
		query.addAggregation(aggregation);
		return this;
	}
	@Override
	public SearchBuilder<T> aggregation(List<AggregationDefinition> aggregations) {
		if(aggregations!=null && aggregations.size()>0) {
			for (AggregationDefinition agg : aggregations) {
				query.addAggregation(agg);
			}
		}
		return this;
	}
	
	@Override
	public SearchBuilder<T> profile() {
		this.profile = new SearchProfile();
	    return this;
	}
	
	@Override
    public SearchBuilder<T> explain() {
        this.explain = new SearchExplain();
        return this;
    }
	
	@Override
	public <T> SearchBuilder<T> builder(Class<T> clazz) {
		return new DefaultSearchBuilder<>(clazz, engine, mapper);
	}

	@Override
	public SearchResult<T> search() {
		SearchContext context = new SearchContext(clazz, index, query, condition, profile, explain);
		SearchResponse response = engine.execute(context);
		try {
			return mapper.map(context, response, clazz);
		} catch (Exception e) {
			throw new SearchMappingException(e);
		}
	}

	@Override
	public CompletableFuture<SearchResult<T>> searchAsync() {
		SearchContext context = new SearchContext(clazz, index, query, condition, profile, explain);
		return engine.executeAsync(context).thenApply(response -> {
			try {
				return mapper.map(context, response, clazz);
			} catch (Exception e) {
				throw new SearchMappingException(e);
			}
		});
	}

	@Override
	public ReactiveSearchBuilder<T> reactive() {
		return new DefaultReactiveSearchBuilder<>(clazz, engine, mapper, index, query, condition, profile, explain);
	}

}