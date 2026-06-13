package com.reyco.dasbx.es.support.annotation.query;

public class EsGeoBoundingBoxParam {
	private Double top;
	private Double left;
	private Double bottom;
	private Double right;

	public EsGeoBoundingBoxParam(Double top, Double left, Double bottom, Double right) {
		super();
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public Double getTop() {
		return top;
	}

	public void setTop(Double top) {
		this.top = top;
	}

	public Double getLeft() {
		return left;
	}

	public void setLeft(Double left) {
		this.left = left;
	}

	public Double getBottom() {
		return bottom;
	}

	public void setBottom(Double bottom) {
		this.bottom = bottom;
	}

	public Double getRight() {
		return right;
	}

	public void setRight(Double right) {
		this.right = right;
	}
}
