package com.reyco.dasbx.common.core.model.po.sys;

import com.reyco.dasbx.model.dto.SimpleListDto;

public class SysLogPO extends SimpleListDto{
	private String name;
	private Long code;
	private Long userId;
	private Long startGmtRequest;
	private Long endGmtRequest;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public Long getStartGmtRequest() {
		return startGmtRequest;
	}
	public void setStartGmtRequest(Long startGmtRequest) {
		this.startGmtRequest = startGmtRequest;
	}
	public Long getEndGmtRequest() {
		return endGmtRequest;
	}
	public void setEndGmtRequest(Long endGmtRequest) {
		this.endGmtRequest = endGmtRequest;
	}
}
