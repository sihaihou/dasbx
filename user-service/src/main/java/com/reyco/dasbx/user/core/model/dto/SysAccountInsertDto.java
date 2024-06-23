package com.reyco.dasbx.user.core.model.dto;

import java.util.List;

import com.reyco.dasbx.model.dto.SimpleInsertDto;

public class SysAccountInsertDto extends SimpleInsertDto{
	private String uid;
	private String nickname;
	private String username;
	private String password;
	private Byte gender;
	private String birthdayDesc;
	private String phone;
	private String email;
	private String address;
	private String remark;
	private List<Long> roleIdList;
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
	public Byte getGender() {
		return gender;
	}
	public void setGender(Byte gender) {
		this.gender = gender;
	}
	public String getBirthdayDesc() {
		return birthdayDesc;
	}
	public void setBirthdayDesc(String birthdayDesc) {
		this.birthdayDesc = birthdayDesc;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<Long> getRoleIdList() {
		return roleIdList;
	}
	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}
}
