package com.reyco.dasbx.es.core.result.mapper;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.reyco.dasbx.es.core.feature.aggregation.parser.AggregationResultParser;
import com.reyco.dasbx.es.core.profile.PipelineProfile;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.SearchHitResult;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.parser.PageResultParser;
import com.reyco.dasbx.es.core.result.parser.SearchHitParser;

public class DefaultResultMapper implements ResultMapper {

	private AggregationResultParser aggParser;

	private PageResultParser pageParser;

	private SearchHitParser searchHitParser;

	public DefaultResultMapper(AggregationResultParser aggParser, SearchHitParser searchHitParser) {
		super();
		this.aggParser = aggParser;
		this.pageParser = new PageResultParser();
		this.searchHitParser = searchHitParser;
	}

	@Override
	public <T> SearchResult<T> map(SearchContext context, SearchResponse response, Class<T> clazz) throws Exception {
		long start = System.nanoTime();

		SearchHits hits = response.getHits();

		List<SearchHitResult<T>> records = parseRecords(hits, clazz, context);

		SearchResult<T> result = new SearchResult<>();
		result.setProfile(context.getProfile());
		result.setExplain(context.getExplain());
		
		result.setRecords(records);

		result.setPage(pageParser.parse(context, response));
		// 4.aggs
		result.setAggregations(aggParser.parse(response.getAggregations(), context.getQuery().getAggregations()));

		long end = System.nanoTime();
		SearchProfile profile = context.getProfile();
		if (profile != null) {
			PipelineProfile p = new PipelineProfile();
			p.setName("ResultMapper");
			p.setTook((end - start) / 1_000_000);
			profile.addPipeline(p);
		}

		return result;
	}

	/**
	 * 解析文档
	 */
	private <T> List<SearchHitResult<T>> parseRecords(SearchHits hits, Class<T> clazz, SearchContext context) {
		List<SearchHitResult<T>> records = new ArrayList<>();
		for (SearchHit hit : hits.getHits()) {
			SearchHitResult<T> result = searchHitParser.parse(hit, clazz, context);
			records.add(result);
		}
		return records;
	}

}
