package com.reyco.dasbx.gateway.core.filter.properties;

import java.util.HashSet;
import java.util.Set;

public class Service {
	
	private Set<String> excludes = new HashSet<>();

	public Set<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(Set<String> excludes) {
		this.excludes = excludes;
	}
	
}
