package com.reyco.dasbx.user.core.provider;

import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;
import com.reyco.dasbx.model.constants.AccountType;

public class TypeLabelProvider implements FacetLabelProvider {

	@Override
	public String getLabel(Object key) {
		String value = key.toString();
		return AccountType.getAccountType(new Byte(value).byteValue()).getRemark();
	} 

}
