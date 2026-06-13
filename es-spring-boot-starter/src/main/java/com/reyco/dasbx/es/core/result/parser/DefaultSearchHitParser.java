package com.reyco.dasbx.es.core.result.parser;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.SearchHit;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import com.alibaba.fastjson.JSON;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.SearchHitResult;
import com.reyco.dasbx.es.core.result.processor.SearchHitProcessor;

public class DefaultSearchHitParser implements SearchHitParser {

	private List<SearchHitProcessor> processors;

	public DefaultSearchHitParser(List<SearchHitProcessor> processors) {
		List<SearchHitProcessor> sortedProcessors = new ArrayList<>(processors);
		AnnotationAwareOrderComparator.sort(sortedProcessors);
		
		this.processors = sortedProcessors;
	}

	@Override
	public <T> SearchHitResult<T> parse(SearchHit hit, Class<T> clazz, SearchContext context) {
		// 1.处理hit
		T entity = parseEntity(context, hit, clazz);

		SearchHitResult<T> result = new SearchHitResult<>();
		result.setSource(entity);
		result.setId(hit.getId());
		result.setIndex(hit.getIndex());
		result.setScore(hit.getScore());

		if (hit.getVersion() >= 0) {
			result.setVersion(hit.getVersion());
		}

		result.setSortValues(hit.getSortValues());
		//
		for (SearchHitProcessor processor : processors) {
			processor.process(context, hit, entity, result);
		}
		return result;
	}

	/**
	 * 解析实体
	 * 
	 * @param hit
	 * @param clazz
	 * @return
	 */
	protected <T> T parseEntity(SearchContext context, SearchHit hit, Class<T> clazz) {
		return JSON.parseObject(hit.getSourceAsString(), clazz);
	}

}
