package com.reyco.dasbx.es.core.query.condition;

import org.springframework.util.StringUtils;

/**
 * 精确匹配
 * @author reyco
 *
 */
public class TermCondition extends AbstractCondition {

    private String field;

    private Object value;

	public TermCondition(String field, Object value) {
		super();
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

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
