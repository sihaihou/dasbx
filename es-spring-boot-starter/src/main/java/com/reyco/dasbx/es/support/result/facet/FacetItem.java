package com.reyco.dasbx.es.support.result.facet;

public class FacetItem {
	/**
	 * bucket key
	 */
	private Object key;

	/**
	 * display value
	 */
	private String label;

	/**
	 * doc count
	 */
	private Long count;
	public FacetItem() {
	}
	
	public FacetItem(Object key, String label) {
		super();
		this.key = key;
		this.label = label;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
