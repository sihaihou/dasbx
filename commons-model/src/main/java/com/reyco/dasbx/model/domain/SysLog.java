package com.reyco.dasbx.model.domain;

import com.reyco.dasbx.model.Base;

public class SysLog extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5483528517909084736L;
	private Long code;
	private Long userId;
	private String service;
	private Byte type;
	private String name;
	private String path;
	private String method;
	private String params;
	private Long times;
	private Long gmtRequest;
	private Long gmtResponse;
	private Byte success;
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Long getTimes() {
		return times;
	}
	public void setTimes(Long times) {
		this.times = times;
	}
	public Long getGmtRequest() {
		return gmtRequest;
	}
	public void setGmtRequest(Long gmtRequest) {
		this.gmtRequest = gmtRequest;
	}
	public Long getGmtResponse() {
		return gmtResponse;
	}
	public void setGmtResponse(Long gmtResponse) {
		this.gmtResponse = gmtResponse;
	}
	public Byte getSuccess() {
		return success;
	}
	public void setSuccess(Byte success) {
		this.success = success;
	}
}
