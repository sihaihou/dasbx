package com.reyco.dasbx.es.support.annotation.query;

public class EsRangeParam {
	private Object gte; // 起始值
    private Object lte; // 结束值
    private Object gt;  // 起始值
	private Object lt;  // 结束值
	public Object getGte() {
		return gte;
	}
	public void setGte(Object gte) {
		this.gte = gte;
	}
	public Object getLte() {
		return lte;
	}
	public void setLte(Object lte) {
		this.lte = lte;
	}
	public Object getGt() {
		return gt;
	}
	public void setGt(Object gt) {
		this.gt = gt;
	}
	public Object getLt() {
		return lt;
	}
	public void setLt(Object lt) {
		this.lt = lt;
	}
}
