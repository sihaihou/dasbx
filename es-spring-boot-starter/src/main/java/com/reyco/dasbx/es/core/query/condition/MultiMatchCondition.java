package com.reyco.dasbx.es.core.query.condition;

import java.util.List;

import org.springframework.util.StringUtils;

/**
 * 多字段搜索
 * @author reyco
 *
 */
public class MultiMatchCondition extends AbstractCondition {

	private Object value;

	private List<String> fields;

	public MultiMatchCondition(Object value,List<String> fields) {
		this.value = value;
		this.fields = fields;
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
	
	public Object getValue() {
		return value;
	}

	public List<String> getFields() {
		return fields;
	}
}
