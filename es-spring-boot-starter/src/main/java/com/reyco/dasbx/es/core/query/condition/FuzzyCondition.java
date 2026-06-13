package com.reyco.dasbx.es.core.query.condition;

import org.springframework.util.StringUtils;

/**
 * 模糊匹配
 * 
 * @author reyco
 *
 */
public class FuzzyCondition extends AbstractCondition {

	private String field;

	private Object value;

	public FuzzyCondition(String field,Object value) {
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

	public Object getValue() {
		return value;
	}
}
