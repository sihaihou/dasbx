package com.reyco.dasbx.gateway.core.filter.properties;

import java.util.HashMap;

public class Filter {
	private int order = 1;
	/**
	 * key: user-service、login-service、open-service、portal-service...
	 */
	private HashMap<String, Service> services = new HashMap<>();
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public HashMap<String, Service> getServices() {
		return services;
	}
	public void setServices(HashMap<String, Service> services) {
		this.services = services;
	}
}
