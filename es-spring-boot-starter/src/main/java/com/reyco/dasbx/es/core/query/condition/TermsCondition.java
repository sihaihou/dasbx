package com.reyco.dasbx.es.core.query.condition;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * IN查询
 * 
 * @author reyco
 *
 */
public class TermsCondition extends AbstractCondition {
	private String field;

	private Collection<?> values;

	public TermsCondition(String field, Collection<?> values) {

		this.field = field;
		this.values = values;
	}
	
	@Override
	public boolean isValid() {
	    if (values == null) {
	        return false;
	    }
	    if (values instanceof Collection) {
	        return !((Collection<?>) values).isEmpty();
	    }
	    if (values.getClass().isArray()) {
	        return Array.getLength(values) > 0;
	    }
	    return true;
	}
	
	public String getField() {
		return field;
	}

	public Collection<?> getValues() {
		return values;
	}
}
