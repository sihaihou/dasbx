package com.reyco.dasbx.common.core.model.po.sys.message;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class SysMessageInsertPO extends SimpleInsertPO{
	private Long type;
	private Long userId;
	private String buzId;
	private Byte buzType;
	private String buzName;
	private String content;
	private String metadata;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public String getBuzId() {
		return buzId;
	}
	public void setBuzId(String buzId) {
		this.buzId = buzId;
	}
	public Byte getBuzType() {
		return buzType;
	}
	public void setBuzType(Byte buzType) {
		this.buzType = buzType;
	}
	public String getBuzName() {
		return buzName;
	}
	public void setBuzName(String buzName) {
		this.buzName = buzName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
}
