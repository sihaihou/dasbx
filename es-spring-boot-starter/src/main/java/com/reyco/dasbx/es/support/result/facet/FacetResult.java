package com.reyco.dasbx.es.support.result.facet;

import java.util.ArrayList;
import java.util.List;

public class FacetResult {
	/**
	 * facet name
	 */
	private String name;

	/**
	 * options
	 */
	private List<FacetItem> items = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FacetItem> getItems() {
		return items;
	}

	public void setItems(List<FacetItem> items) {
		this.items = items;
	}
}
