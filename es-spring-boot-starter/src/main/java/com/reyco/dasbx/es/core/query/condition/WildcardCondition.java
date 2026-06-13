package com.reyco.dasbx.es.core.query.condition;

import org.springframework.util.StringUtils;

public class WildcardCondition extends AbstractCondition {

	private String field;

	private String value;

	public WildcardCondition(String field, String value) {
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
