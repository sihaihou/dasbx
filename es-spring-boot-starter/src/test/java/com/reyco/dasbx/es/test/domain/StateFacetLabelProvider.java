package com.reyco.dasbx.es.test.domain;

import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;

public class StateFacetLabelProvider implements FacetLabelProvider{
	public StateFacetLabelProvider() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getLabel(Object key) {
		byte type = Byte.parseByte(key.toString());
		switch (type) {
		case 1:
			return "禁用";

		case 0:
			return "正常";

		default:
			return "未知";
		}
	}

}
