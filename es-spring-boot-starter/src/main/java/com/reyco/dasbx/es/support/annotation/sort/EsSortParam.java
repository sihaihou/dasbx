package com.reyco.dasbx.es.support.annotation.sort;

public class EsSortParam {
	/**
	 * 是否启用
	 */
	private Boolean enabled = true;
	/**
	 * 类型
	 */
	private EsSortType type = EsSortType.FIELD;
	/**
	 * 排序优先级
	 */
	private int order = 1;
	/**
	 * 只有ASC、DESC  org.elasticsearch.search.sort.SortOrder
	 */
	private String sortOrder = "ASC";

	
	/** [地理位置排序] 纬度 */
	private double latitude;
	 /** [地理位置排序] 经度 */
	private double longitude;
	/** [距离排序专属]：距离单位，默认米(m)，支持 km org.elasticsearch.common.unit.DistanceUnit*/
	private String unit = "m";
	
	
	/** [脚本排序专属]：内联脚本代码，例如 "doc['price'].value * doc['discount'].value" */
	private String script;
	 /** [脚本排序] 脚本返回值类型，默认数值型 */
	private String scriptType;
	
	public Boolean isEnabled() {
		return enabled;
	}
	
	public EsSortParam setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	
	public int getOrder() {
		return order;
	}
	public EsSortParam setOrder(int order) {
		this.order = order;
		return this;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public EsSortParam setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
		return this;
	}
	public EsSortType getType() {
		return type;
	}
	public EsSortParam setType(EsSortType type) {
		this.type = type;
		return this;
	}
	public double getLatitude() {
		return latitude;
	}
	public EsSortParam setLatitude(double latitude) {
		this.latitude = latitude;
		return this;
	}
	public double getLongitude() {
		return longitude;
	}
	public EsSortParam setLongitude(double longitude) {
		this.longitude = longitude;
		return this;
	}
	public String getUnit() {
		return unit;
	}
	public EsSortParam setUnit(String unit) {
		this.unit = unit;
		return this;
	}
	public String getScript() {
		return script;
	}
	public EsSortParam setScript(String script) {
		this.script = script;
		return this;
	}
	public String getScriptType() {
		return scriptType;
	}
	public EsSortParam setScriptType(String scriptType) {
		this.scriptType = scriptType;
		return this;
	}
	
}
