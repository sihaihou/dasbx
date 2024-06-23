package com.reyco.dasbx.commons.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ToString implements Serializable {

	private static final long serialVersionUID = -7157711665143774407L;
	private static final Collection<String> fieldNames = new ArrayList<String>();

	public static String toString(Object obj) {
		if (fieldNames.size() == 0) {
			return ToStringBuilder.reflectionToString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
		} else {
			return new ReflectionToStringBuilder(obj, ToStringStyle.SHORT_PREFIX_STYLE)
					.setExcludeFieldNames(fieldNames.toArray(new String[fieldNames.size()])).toString();
		}
	}

	public static void addFilterField(String fieldName) {
		fieldNames.add(fieldName);
	}

	@Override
	public String toString() {
		return toString(this);
	}
}
