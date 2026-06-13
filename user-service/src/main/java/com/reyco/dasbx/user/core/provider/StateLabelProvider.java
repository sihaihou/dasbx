package com.reyco.dasbx.user.core.provider;

import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;

public class StateLabelProvider implements FacetLabelProvider {

	@Override
	public String getLabel(Object key) {
		String value = key.toString();
		return value.equals("0")?"正常":"禁用";
	} 

}
