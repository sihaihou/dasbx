package com.reyco.dasbx.resource.core.model;

import java.util.Map;

public interface ResourceDefinition {
	
	String getToken();
	
	String getApplication();
	
	String getResource();
	
	String getDescription();
	
	boolean isController();
	
	String getMethod();
	
	Map<String,Object> getParameters();
	
	Long getStartTime();
	
	Long getEndTime();
	
	Boolean isSuccess();
}
