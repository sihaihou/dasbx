package com.reyco.dasbx.es.support.result.facet;

public class FacetDefinition {
	/**
	 * facet name
	 */
	private String name;

	/**
	 * field
	 */
	private String field;

	/**
	 * label converter
	 */
	private Class<? extends FacetLabelProvider> providerClass;

	public String getName() {
		return name;
	}

	public FacetDefinition setName(String name) {
		this.name = name;
		return this;
	}

	public String getField() {
		return field;
	}

	public FacetDefinition setField(String field) {
		this.field = field;
		return this;
	}

	public FacetDefinition setProviderClass(Class<? extends FacetLabelProvider> providerClass) {
		this.providerClass = providerClass;
		return this;
	}
	public Class<? extends FacetLabelProvider> getProviderClass() {
		return providerClass;
	}
}
