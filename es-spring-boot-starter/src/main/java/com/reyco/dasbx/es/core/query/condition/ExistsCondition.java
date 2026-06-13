package com.reyco.dasbx.es.core.query.condition;

/**
 * 字段存在
 * 
 * @author reyco
 *
 */
public class ExistsCondition extends AbstractCondition {
	private String field;

	public ExistsCondition(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}
}
