package com.reyco.dasbx.es.core.query;

import java.util.ArrayList;
import java.util.List;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.highlight.HighlightDefinition;
import com.reyco.dasbx.es.core.query.condition.Condition;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.result.page.PageDefinition;

public class SearchQuery {
	/**
	 * alias
	 */
	private String index;

	/**
	 * 条件
	 */
	private Condition condition;
	/**
	 * 
	 */
	private String keyword;

	/**
	 * 分页
	 */
	private PageDefinition page;
	
	/**
	 * 路由
	 */
	private String routing;
	
	/**
	 * 排序
	 */
	private List<SortDefinition> sorts = new ArrayList<>();

	/**
	 * 高亮配置
	 */
	private List<HighlightDefinition> highlights = new ArrayList<>();
	
	/**
	 * 聚合
	 */
	private List<AggregationDefinition> aggregations = new ArrayList<>();
	
	/**
	 * 包含字段
	 */
	private List<String> includeFields;

	/**
	 * 排除字段
	 */
	private List<String> excludeFields;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public void addHighlight(HighlightDefinition highlight) {
		this.highlights.add(highlight);
	}

	public void addAggregation(AggregationDefinition agg) {
		aggregations.add(agg);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public PageDefinition getPage() {
		return page;
	}

	public void setPage(PageDefinition page) {
		this.page = page;
	}
	public String getRouting() {
		return routing;
	}
	public void setRouting(String routing) {
		this.routing = routing;
	}
	
	public List<SortDefinition> getSorts() {
		return sorts;
	}
	public void setSorts(List<SortDefinition> sorts) {
		this.sorts = sorts;
	}

	public List<HighlightDefinition> getHighlights() {
		return highlights;
	}

	public void setHighlights(List<HighlightDefinition> highlights) {
		this.highlights = highlights;
	}

	public List<AggregationDefinition> getAggregations() {
		return aggregations;
	}
	
	public List<String> getIncludeFields() {
		return includeFields;
	}

	public void setIncludeFields(List<String> includeFields) {
		this.includeFields = includeFields;
	}

	public List<String> getExcludeFields() {
		return excludeFields;
	}

	public void setExcludeFields(List<String> excludeFields) {
		this.excludeFields = excludeFields;
	}
}
