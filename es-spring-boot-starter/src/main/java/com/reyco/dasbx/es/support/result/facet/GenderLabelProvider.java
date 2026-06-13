package com.reyco.dasbx.es.support.result.facet;

public class GenderLabelProvider implements FacetLabelProvider {
	public GenderLabelProvider() {
	}

	@Override
	public String getLabel(Object key) {
		Integer gender = Integer.valueOf(key.toString());
		switch (gender) {
		case 1:
			return "男";

		case 0:
			return "女";

		default:
			return "未知";
		}
	}
}
