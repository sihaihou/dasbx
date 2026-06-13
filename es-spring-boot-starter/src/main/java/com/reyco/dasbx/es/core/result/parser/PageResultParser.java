package com.reyco.dasbx.es.core.result.parser;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.page.OffsetPageDefinition;
import com.reyco.dasbx.es.core.result.page.OffsetPageResult;
import com.reyco.dasbx.es.core.result.page.PageDefinition;
import com.reyco.dasbx.es.core.result.page.PageResult;
import com.reyco.dasbx.es.core.result.page.ScrollPageDefinition;
import com.reyco.dasbx.es.core.result.page.ScrollPageResult;
import com.reyco.dasbx.es.core.result.page.SearchAfterPageDefinition;
import com.reyco.dasbx.es.core.result.page.SearchAfterPageResult;

public class PageResultParser {
	
	public PageResult parse(SearchContext context, SearchResponse response) {
		PageDefinition page = context.getQuery().getPage();
		if (page == null) {
			return null;
		}

		/*
		 * offset page
		 */
		if (page instanceof OffsetPageDefinition) {
			OffsetPageDefinition p = (OffsetPageDefinition) page;
			OffsetPageResult result = new OffsetPageResult();
			result.setPageNum(p.getPageNum());
			result.setPageSize(p.getPageSize());
			result.setTotal(response.getHits().getTotalHits().value);
			return result;
		}

		/*
		 * search after
		 */
		if (page instanceof SearchAfterPageDefinition) {
			SearchAfterPageDefinition p = (SearchAfterPageDefinition) page;
			SearchAfterPageResult result = new SearchAfterPageResult();
			result.setPageSize(p.getPageSize());
			SearchHit[] hits = response.getHits().getHits();
			if (hits.length > 0) {
				SearchHit last = hits[hits.length - 1];
				result.setNextSearchAfter(last.getSortValues());
			}

			return result;
		}

		/*
		 * scroll
		 */
		if (page instanceof ScrollPageDefinition) {
			ScrollPageResult result = new ScrollPageResult();
			result.setScrollId(response.getScrollId());
			result.setPageSize(response.getHits().getHits().length);
			return result;
		}

		return null;
	}
}
