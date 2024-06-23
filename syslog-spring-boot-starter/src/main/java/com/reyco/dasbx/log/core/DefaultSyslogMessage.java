package com.reyco.dasbx.log.core;

import java.io.Serializable;

public class DefaultSyslogMessage implements Serializable,SyslogMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1079569435479610570L;
	private String correlationDataId;
	private String code;
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
	@Override
	public String getCorrelationDataId() {
		return correlationDataId;
	}
	public void setCorrelationDataId(String correlationDataId) {
		this.correlationDataId = correlationDataId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	@Override
	public String toString() {
		return "SysLogMessage [correlationDataId=" + correlationDataId + ", code=" + code + ", service=" + service
				+ ", type=" + type + ", name=" + name + ", path=" + path + ", method=" + method + ", params=" + params
				+ ", times=" + times + ", gmtRequest=" + gmtRequest + ", gmtResponse=" + gmtResponse + ", success="
				+ success + "]";
	}
}
