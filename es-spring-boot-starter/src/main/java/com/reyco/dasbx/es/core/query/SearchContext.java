package com.reyco.dasbx.es.core.query;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.reyco.dasbx.es.core.explain.SearchExplain;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.condition.Condition;

public class SearchContext {

	private Class<?> clazz;
	/**
	 * 索引
	 */
	private String index;
	/**
	 * alias解析后的真实索引
	 */
	private List<String> resolvedIndices = new ArrayList<>();

	/**
	 * 条件
	 */
	private Condition condition;
	/**
	 * 业务请求参数
	 */
	private SearchQuery query;
	/**
	 * es
	 */
	private SearchRequest request;

	private SearchResponse response;

	private SearchSourceBuilder sourceBuilder;

	/*------------------------------日志相关属性----------------------------*/
	/**
	 * 性能监控
	 */
	private SearchProfile profile;
	/**
	 * 用于调试
	 */
	private SearchExplain explain;

	public SearchContext(Class<?> clazz, String index, SearchQuery query, Condition condition,SearchProfile profile,SearchExplain explain) {
		this.clazz = clazz;
		this.index = index;
		this.query = query;
		this.condition = condition;
		
		this.profile = profile;
		this.explain = explain;

		this.sourceBuilder = new SearchSourceBuilder();
	}

	public SearchRequest getRequest() {
		if (this.request == null) {
			this.request = new SearchRequest();
		}
		return this.request;
	}

	public boolean profileEnabled() {
		return profile != null;
	}

	public boolean explainEnabled() {
		return explain != null;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getIndex() {
		return index;
	}

	public Condition getCondition() {
		return condition;
	}

	public SearchQuery getQuery() {
		return query;
	}

	public void setQuery(SearchQuery query) {
		this.query = query;
	}

	public SearchResponse getResponse() {
		return response;
	}

	public void setResponse(SearchResponse response) {
		this.response = response;
	}

	public SearchSourceBuilder getSourceBuilder() {
		return sourceBuilder;
	}

	public List<String> getResolvedIndices() {
		return resolvedIndices;
	}

	public void setResolvedIndices(List<String> resolvedIndices) {
		this.resolvedIndices = resolvedIndices;
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
}
