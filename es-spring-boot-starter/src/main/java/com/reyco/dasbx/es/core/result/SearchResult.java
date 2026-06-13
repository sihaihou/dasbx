package com.reyco.dasbx.es.core.result;

import java.util.List;

import com.reyco.dasbx.es.core.explain.SearchExplain;
import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.result.page.PageResult;

public class SearchResult<T> {

	/**
	 * 数据列表
	 */
	private List<SearchHitResult<T>> records;

	/**
	 * 分页结果
	 */
	private PageResult page;

	/**
	 * 聚合树
	 */
	private List<AggregationNode> aggregations;

	private SearchProfile profile;

	private SearchExplain explain;

	public List<SearchHitResult<T>> getRecords() {
		return records;
	}

	public void setRecords(List<SearchHitResult<T>> records) {
		this.records = records;
	}

	public PageResult getPage() {
		return page;
	}

	public void setPage(PageResult page) {
		this.page = page;
	}

	public List<AggregationNode> getAggregations() {
		return aggregations;
	}

	public void setAggregations(List<AggregationNode> aggregations) {
		this.aggregations = aggregations;
	}

	public SearchProfile getProfile() {
		return profile;
	}

	public void setProfile(SearchProfile profile) {
		this.profile = profile;
	}

	public SearchExplain getExplain() {
		return explain;
	}

	public void setExplain(SearchExplain explain) {
		this.explain = explain;
	}

	@Override
	public String toString() {
		return "SearchResult [records=" + records + ", page=" + page + ", aggregations=" + aggregations + "]";
	}
}
