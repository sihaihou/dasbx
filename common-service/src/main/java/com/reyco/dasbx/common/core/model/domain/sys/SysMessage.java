package com.reyco.dasbx.common.core.model.domain.sys;

import com.reyco.dasbx.model.Base;

public class SysMessage extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1358676351079247504L;

	private Long type;
	private Long userId;
	private Long buzId;
	private Byte buzType;
	private String buzName;
	private String content;
	private Byte read;
	private Long gmtRead;
	private Long readBy;
	private Byte handle;
	private Long gmtHandle;
	private Long handleBy;
	public Long getType() {
		return type;
	}
	public void setType(Long type) {
		this.type = type;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Long getGmtRead() {
		return gmtRead;
	}
	public void setGmtRead(Long gmtRead) {
		this.gmtRead = gmtRead;
	}
	public Long getReadBy() {
		return readBy;
	}
	public void setReadBy(Long readBy) {
		this.readBy = readBy;
	}
	public Byte getHandle() {
		return handle;
	}
	public void setHandle(Byte handle) {
		this.handle = handle;
	}
	public Long getGmtHandle() {
		return gmtHandle;
	}
	public void setGmtHandle(Long gmtHandle) {
		this.gmtHandle = gmtHandle;
	}
	public Long getHandleBy() {
		return handleBy;
	}
	public void setHandleBy(Long handleBy) {
		this.handleBy = handleBy;
	}
}
