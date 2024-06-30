package com.reyco.dasbx.resource.core.model;

import java.util.Map;

public class DefaultResourceDefinition implements ResourceDefinition{
	private String token;
	private String application;
	private String resource;
	private String description;
	private Boolean isController;
	private String method;
	private Map<String,Object> parameters;
	private Long startTime;
	private Long endTime;
	private Boolean success;
	private String exceptionInfo;
	
	@Override
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	@Override
	public String getApplication() {
		return application;
	}
	@Override
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	@Override
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public boolean isController() {
		return isController;
	}
	public void setIsController(Boolean isController) {
		this.isController = isController;
	}
	@Override
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	@Override
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	@Override
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	@Override
	public Boolean isSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	@Override
	public String getExceptionInfo() {
		return this.exceptionInfo;
	}
	public void setExceptionInfo(String exceptionInfo) {
		this.exceptionInfo = exceptionInfo;
	}
	@Override
	public String toString() {
		return "DefaultResourceDefinition [token=" + token + ", application=" + application + ", resource=" + resource
				+ ", description=" + description + ", isController=" + isController + ", method=" + method
				+ ", parameters=" + parameters + ", startTime=" + startTime + ", endTime=" + endTime + ", success="
				+ success + ", exceptionInfo=" + exceptionInfo + "]";
	}
}
