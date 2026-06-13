package com.reyco.dasbx.es.core.query.condition;

public class RangeCondition extends AbstractCondition {

	private String field;

	private Object gte;

	private Object lte;

	private Object gt;

	private Object lt;

	public RangeCondition(String field) {
	}
	
	@Override
	public boolean isValid() {
	    return gt != null
	            || gte != null
	            || lt != null
	            || lte != null;
	}
	
	public RangeCondition gte(Object gte) {
		this.gte = gte;
		return this;
	}

	public RangeCondition lte(Object lte) {
		this.lte = lte;
		return this;
	}

	public RangeCondition gt(Object gt) {
		this.gt = gt;
		return this;
	}

	public RangeCondition lt(Object lt) {
		this.lt = lt;
		return this;
	}

	public String getField() {
		return field;
	}

	public Object getGte() {
		return gte;
	}

	public Object getLte() {
		return lte;
	}

	public Object getGt() {
		return gt;
	}

	public Object getLt() {
		return lt;
	}

}
