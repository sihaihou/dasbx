package com.reyco.dasbx.es.core.result.page;

import org.elasticsearch.common.unit.TimeValue;

public class ScrollPageDefinition implements PageDefinition {

	/**
	 * scroll alive
	 */
	private TimeValue keepAlive = TimeValue.timeValueMinutes(1);

	/**
	 * scrollId
	 */
	private String scrollId;

	/**
	 * size
	 */
	private Integer pageSize = 10;

	public TimeValue getKeepAlive() {
		return keepAlive;
	}

	public ScrollPageDefinition setKeepAlive(TimeValue keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public String getScrollId() {
		return scrollId;
	}

	public ScrollPageDefinition setScrollId(String scrollId) {
		this.scrollId = scrollId;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public ScrollPageDefinition setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
}
