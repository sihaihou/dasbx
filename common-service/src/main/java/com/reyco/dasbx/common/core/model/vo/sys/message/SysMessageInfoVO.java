package com.reyco.dasbx.common.core.model.vo.sys.message;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.vo.InfoVO;

public class SysMessageInfoVO implements InfoVO{
	private Long id;
	private Long userId;
	private String username;
	private Long type;
	private Long buzId;
	private Byte buzType;
	private String buzName;
	private String content;
	private Byte read;
	private Long gmtRead;
	private String gmtReadDesc;
	private Long readBy;
	private String readByDesc;
	private Byte handle;
	private Long gmtHandle;
	private String gmtHandleDesc;
	private Long handleBy;
	private String handleByDesc;
	private Long gmtCreate;
	private String gmtCreateDesc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public Long getGmtRead() {
		return gmtRead;
	}
	public void setGmtRead(Long gmtRead) {
		this.gmtRead = gmtRead;
		if(this.gmtRead!=null) {
			this.gmtReadDesc = Dasbx.getDateByTimeZone(gmtRead);
		}
	}
	public String getGmtReadDesc() {
		return gmtReadDesc;
	}
	public void setGmtReadDesc(String gmtReadDesc) {
		this.gmtReadDesc = gmtReadDesc;
	}
	public Long getReadBy() {
		return readBy;
	}
	public void setReadBy(Long readBy) {
		this.readBy = readBy;
	}
	public String getReadByDesc() {
		return readByDesc;
	}
	public void setReadByDesc(String readByDesc) {
		this.readByDesc = readByDesc;
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
		if(this.gmtHandle!=null) {
			this.gmtHandleDesc = Dasbx.getDateByTimeZone(gmtHandle);
		}
	}
	public String getGmtHandleDesc() {
		return gmtHandleDesc;
	}
	public void setGmtHandleDesc(String gmtHandleDesc) {
		this.gmtHandleDesc = gmtHandleDesc;
	}
	public Long getHandleBy() {
		return handleBy;
	}
	public void setHandleBy(Long handleBy) {
		this.handleBy = handleBy;
	}
	public String getHandleByDesc() {
		return handleByDesc;
	}
	public void setHandleByDesc(String handleByDesc) {
		this.handleByDesc = handleByDesc;
	}
	public Long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
		if(this.gmtCreate!=null) {
			this.gmtCreateDesc = Dasbx.getDateByTimeZone(gmtCreate);
		}
	}
	public String getGmtCreateDesc() {
		return gmtCreateDesc;
	}
	public void setGmtCreateDesc(String gmtCreateDesc) {
		this.gmtCreateDesc = gmtCreateDesc;
	}
}
