package com.reyco.dasbx.es.core.query.geo.condition;

import com.reyco.dasbx.es.core.query.condition.AbstractCondition;

public class GeoBoundingBoxCondition extends AbstractCondition {

	/**
	 * field
	 */
	private String field;

	/**
	 * top
	 */
	private Double top;

	/**
	 * left
	 */
	private Double left;

	/**
	 * bottom
	 */
	private Double bottom;

	/**
	 * right
	 */
	private Double right;
	
	@Override
	public boolean isValid() {
	    return top != null
	            && left != null
	            && bottom != null
	            && right != null;
	}
	
	public String getField() {
		return field;
	}

	public GeoBoundingBoxCondition setField(String field) {
		this.field = field;
		return this;
	}

	public Double getTop() {
		return top;
	}

	public GeoBoundingBoxCondition setTop(Double top) {
		this.top = top;
		return this;
	}

	public Double getLeft() {
		return left;
	}

	public GeoBoundingBoxCondition setLeft(Double left) {
		this.left = left;
		return this;
	}

	public Double getBottom() {
		return bottom;
	}

	public GeoBoundingBoxCondition setBottom(Double bottom) {
		this.bottom = bottom;
		return this;
	}

	public Double getRight() {
		return right;
	}

	public GeoBoundingBoxCondition setRight(Double right) {
		this.right = right;
		return this;
	}
}
