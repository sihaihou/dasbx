package com.reyco.dasbx.es.core.query.condition;

import org.springframework.util.StringUtils;

/**
 * 前缀匹配
 * 
 * @author reyco
 *
 */
public class PrefixCondition extends AbstractCondition {

	private String field;

	private String value;

	public PrefixCondition(String field,String value) {
		this.field = field;
		this.value = value;
	}
	
	@Override
	public boolean isValid() {
		if (value == null) {
			return false;
		}
		if (value instanceof String) {
			return StringUtils.hasText((String) value);
		}
		return true;
	}
	
	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}
}
