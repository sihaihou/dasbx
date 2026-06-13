package com.reyco.dasbx.es.support.execution.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationDefinition;
import com.reyco.dasbx.es.core.feature.highlight.HighlightDefinition;
import com.reyco.dasbx.es.core.query.condition.BoolCondition;
import com.reyco.dasbx.es.core.query.sort.SortDefinition;
import com.reyco.dasbx.es.core.result.page.PageDefinition;
import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;

public class EsExecutionContext {

	private String index;
	
	private String routing;

	private List<String> includeFields;
	
	private List<String> excludeFields;
	
	private BoolCondition boolCondition;
	
	private PageDefinition page;

	private List<SortDefinition> sorts;
	
	/**
	 * key:name, value:FacetLabelProvider.class
	 */
	private List<HighlightDefinition> highlights;
	
	private List<AggregationDefinition> aggregations;
	
	private Map<String, Class<? extends FacetLabelProvider>> providerMap;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getRouting() {
		return routing;
	}

	public void setRouting(String routing) {
		this.routing = routing;
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

	public BoolCondition getBoolCondition() {
		return boolCondition;
	}

	public void setBoolCondition(BoolCondition boolCondition) {
		this.boolCondition = boolCondition;
	}

	public PageDefinition getPage() {
		return page;
	}

	public void setPage(PageDefinition page) {
		this.page = page;
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

	public void setAggregations(List<AggregationDefinition> aggregations) {
		this.aggregations = aggregations;
	}

	public Map<String, Class<? extends FacetLabelProvider>> getProviderMap() {
		return providerMap;
	}

	public void setProviderMap(Map<String, Class<? extends FacetLabelProvider>> providerMap) {
		this.providerMap = providerMap;
	}
	
}
