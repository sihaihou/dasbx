package com.reyco.dasbx.es.core.query.condition;

import org.springframework.util.StringUtils;

/**
 * 全文匹配
 * 
 * @author reyco
 *
 */
public class MatchCondition extends AbstractCondition {

	private String field;

	private Object value;

	public MatchCondition() {
		// TODO Auto-generated constructor stub
	}

	public MatchCondition(String field, Object value) {
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

	public MatchCondition setField(String field) {
		this.field = field;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public MatchCondition setValue(Object value) {
		this.value = value;
		return this;
	}
}
