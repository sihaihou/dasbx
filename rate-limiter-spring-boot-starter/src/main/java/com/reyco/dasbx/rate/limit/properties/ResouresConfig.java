package com.reyco.dasbx.rate.limit.properties;

public class ResouresConfig {
	private int window = 1;
	private String unit = "SECONDS";
    private int maxRequests = 100;
    private String description = "描述资源信息";
	public int getWindow() {
		return window;
	}
	public void setWindow(int window) {
		this.window = window;
	}
	public int getMaxRequests() {
		return maxRequests;
	}
	public void setMaxRequests(int maxRequests) {
		this.maxRequests = maxRequests;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
