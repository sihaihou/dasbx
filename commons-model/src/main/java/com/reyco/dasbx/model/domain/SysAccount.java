package com.reyco.dasbx.model.domain;

import com.reyco.dasbx.model.Base;

public class SysAccount extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5821011956312696103L;
	private String uid;
	private Long developerId;
	private String faceUri;
	private String nickname;
	private String username;
	private String password;
	private String salt;
	private Integer type;
	private Integer integral;
	private Byte gender;
	private Long birthday;
	private String phone;
	private String email;
	private String address;
	private Byte state;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Long getDeveloperId() {
		return developerId;
	}
	public void setDeveloperId(Long developerId) {
		this.developerId = developerId;
	}
	public String getFaceUri() {
		return faceUri;
	}
	public void setFaceUri(String faceUri) {
		this.faceUri = faceUri;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public Byte getGender() {
		return gender;
	}
	public void setGender(Byte gender) {
		this.gender = gender;
	}
	public Long getBirthday() {
		return birthday;
	}
	public void setBirthday(Long birthday) {
		this.birthday = birthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
}
