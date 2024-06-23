package com.reyco.dasbx.login.core.model.domain;

import com.reyco.dasbx.model.Base;

/**
 * 第三方用户
 * @author reyco
 *
 */
public class Accounts extends Base{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3175648085623510485L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 
	 */
	private String uid;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Long gmtCreate;
	/**
	 * 修改时间
	 */
	private Long gmtModified;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Long getGmtModified() {
		return gmtModified;
	}
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
	}
}
