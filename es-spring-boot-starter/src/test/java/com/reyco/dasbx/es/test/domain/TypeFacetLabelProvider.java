package com.reyco.dasbx.es.test.domain;

import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;

public class TypeFacetLabelProvider implements FacetLabelProvider{
	public TypeFacetLabelProvider() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getLabel(Object key) {
		byte type = Byte.parseByte(key.toString());
		switch (type) {
		case (byte)1:
			return "QQ用户";

		case (byte)2:
			return "微博用户";
		case (byte)3:
			return "百度用户";
		default:
			return "系统用户";
		}
	}

}
