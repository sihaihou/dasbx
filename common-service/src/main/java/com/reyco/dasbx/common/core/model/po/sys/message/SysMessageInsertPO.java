package com.reyco.dasbx.common.core.model.po.sys.message;

import com.reyco.dasbx.model.po.SimpleInsertPO;

public class SysMessageInsertPO extends SimpleInsertPO{
	private Long type;
	private Long userId;
	private Long buzId;
	private Byte buzType;
	private String buzName;
	private String content;
	private Byte read;
	private Byte handle;
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
	public Long getBuzId() {
		return buzId;
	}
	public void setBuzId(Long buzId) {
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
	public Byte getRead() {
		return read;
	}
	public void setRead(Byte read) {
		this.read = read;
	}
	public Byte getHandle() {
		return handle;
	}
	public void setHandle(Byte handle) {
		this.handle = handle;
	}
}
