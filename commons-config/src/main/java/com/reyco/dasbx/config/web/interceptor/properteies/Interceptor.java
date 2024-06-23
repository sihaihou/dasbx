package com.reyco.dasbx.config.web.interceptor.properteies;

import java.util.ArrayList;
import java.util.List;

public class Interceptor {
	private Integer order;
	private List<String> exclude = new ArrayList<String>();
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public List<String> getExclude() {
		return exclude;
	}

	public void setExclude(List<String> exclude) {
		this.exclude = exclude;
	}
}
