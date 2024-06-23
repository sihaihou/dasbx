package com.reyco.dasbx.common.core.model.vo.sys;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.vo.BaseVO;

public class SysLogListVO implements BaseVO{
	private Long id;
	private Long code;
	private String service;
	private Byte type;
	private String name;
	private String path;
	private String method;
	private String params;
	private Long times;
	private Long gmtRequest;
	private String gmtRequestDesc;
	private Long gmtResponse;
	private String gmtResponseDesc;
	private Byte success;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
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
		if(gmtRequest!=null) {
			this.gmtRequestDesc = Dasbx.getDateByTimeZone(gmtRequest);
		}
	}
	public String getGmtRequestDesc() {
		return gmtRequestDesc;
	}
	public void setGmtRequestDesc(String gmtRequestDesc) {
		this.gmtRequestDesc = gmtRequestDesc;
	}
	public Long getGmtResponse() {
		return gmtResponse;
	}
	public void setGmtResponse(Long gmtResponse) {
		this.gmtResponse = gmtResponse;
		if(gmtResponse!=null) {
			this.gmtResponseDesc = Dasbx.getDateByTimeZone(gmtResponse);
		}
	}
	public String getGmtResponseDesc() {
		return gmtResponseDesc;
	}
	public void setGmtResponseDesc(String gmtResponseDesc) {
		this.gmtResponseDesc = gmtResponseDesc;
	}
	public Byte getSuccess() {
		return success;
	}
	public void setSuccess(Byte success) {
		this.success = success;
	}
}
