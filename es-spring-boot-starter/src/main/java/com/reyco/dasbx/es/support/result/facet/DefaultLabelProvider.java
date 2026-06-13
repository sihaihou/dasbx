package com.reyco.dasbx.es.support.result.facet;

public class DefaultLabelProvider implements FacetLabelProvider{

	@Override
	public String getLabel(Object key) {
		return key.toString();
	}

}
