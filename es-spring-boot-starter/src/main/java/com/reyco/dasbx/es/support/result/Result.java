package com.reyco.dasbx.es.support.result;

import java.util.List;

import com.reyco.dasbx.commons.utils.Page;
import com.reyco.dasbx.es.support.result.facet.FacetResult;

public class Result<T> {
	
	private List<FacetResult> aggregations;
	
	private Page<T> page;

	public List<FacetResult> getAggregations() {
		return aggregations;
	}

	public void setAggregations(List<FacetResult> aggregations) {
		this.aggregations = aggregations;
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "Result [aggregations=" + aggregations + ", page=" + page + "]";
	}
	
}
