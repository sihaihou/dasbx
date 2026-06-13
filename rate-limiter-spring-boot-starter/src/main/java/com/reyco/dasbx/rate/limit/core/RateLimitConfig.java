package com.reyco.dasbx.rate.limit.core;

import java.util.concurrent.TimeUnit;

public class RateLimitConfig {
	private int window; // 时间窗口大小
	private int maxRequests; // 最大请求次数
	private TimeUnit unit; // 时间单位
	public RateLimitConfig() {
		// TODO Auto-generated constructor stub
	}

	public RateLimitConfig(int window, int maxRequests, TimeUnit unit) {
		this.window = window;
		this.maxRequests = maxRequests;
		this.unit = unit;
	}

	public String getDescription() {
		return String.format("%d%s内最多%d次", window, getChineseUnit(unit), maxRequests);
	}

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

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

	private String getChineseUnit(TimeUnit unit) {
		switch (unit) {
		case SECONDS:
			return "秒";
		case MINUTES:
			return "分钟";
		case HOURS:
			return "小时";
		case DAYS:
			return "天";
		default:
			return unit.name().toLowerCase();
		}
	}

	public long getWindowMillis() {
		return unit.toMillis(window);
	}
}
