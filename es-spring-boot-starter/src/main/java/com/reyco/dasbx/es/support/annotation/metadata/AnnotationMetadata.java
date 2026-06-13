package com.reyco.dasbx.es.support.annotation.metadata;

import java.util.ArrayList;
import java.util.List;

public class AnnotationMetadata {

	private String index;
	
	private List<RoutingMetadata> routings = new ArrayList<>();

	private List<QueryMetadata> queries = new ArrayList<>();
	
	private List<SourceMetadata> sources = new ArrayList<>();
	
	private List<PageMetadata> pages = new ArrayList<>();
	
	private List<SortMetadata> sorts = new ArrayList<>();
	
	private List<HighlightMetadata> highlights = new ArrayList<>();

	private List<AggregationMetadata> aggregations = new ArrayList<>();

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<RoutingMetadata> getRoutings() {
		return routings;
	}

	public void setRoutings(List<RoutingMetadata> routings) {
		this.routings = routings;
	}

	public List<QueryMetadata> getQueries() {
		return queries;
	}

	public void setQueries(List<QueryMetadata> queries) {
		this.queries = queries;
	}

	public List<SourceMetadata> getSources() {
		return sources;
	}

	public void setSources(List<SourceMetadata> sources) {
		this.sources = sources;
	}

	public List<PageMetadata> getPages() {
		return pages;
	}

	public void setPages(List<PageMetadata> pages) {
		this.pages = pages;
	}

	public List<SortMetadata> getSorts() {
		return sorts;
	}

	public void setSorts(List<SortMetadata> sorts) {
		this.sorts = sorts;
	}

	public List<HighlightMetadata> getHighlights() {
		return highlights;
	}

	public void setHighlights(List<HighlightMetadata> highlights) {
		this.highlights = highlights;
	}

	public List<AggregationMetadata> getAggregations() {
		return aggregations;
	}

	public void setAggregations(List<AggregationMetadata> aggregations) {
		this.aggregations = aggregations;
	}

}
