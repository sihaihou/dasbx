package com.reyco.dasbx.constant.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix=ConstantProperties.CONSTANT_PREFIX)
public class ConstantProperties {
	
	public final static String CONSTANT_PREFIX = "reyco.dasbx";
	
	private Map<String,Object> constants = new HashMap<>();

	public Map<String, Object> getConstants() {
		return constants;
	}

	public void setConstants(Map<String, Object> constants) {
		this.constants = constants;
	}
}
