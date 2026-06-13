package com.reyco.dasbx.es.test.domain;

import java.util.List;

import com.reyco.dasbx.es.support.result.record.EsHighlightResult;

public class SysAccountInfoVO {
	private Long id;
	private String uid;
	private Long developerId;
	private String faceUri;
	private String nickname;
	private String username;
	private Byte state;
	private Byte type;
	private Integer integral;
	private Byte gender;
	private Long birthday;
	private String birthdayDesc;
	private String phone;
	private String email;
	private String address;
	private String remark;
	private List<Long> roleIdList;
	
	@EsHighlightResult("nickname")
	private String highlightNickname;
	@EsHighlightResult("username")
	private String highlightUsername;
	@EsHighlightResult("phone")
	private String highlightPhone;
	@EsHighlightResult("email")
	private String highlightEmail;
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
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
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
	public String getHighlightNickname() {
		return highlightNickname;
	}
	public void setHighlightNickname(String highlightNickname) {
		this.highlightNickname = highlightNickname;
	}
	public String getHighlightUsername() {
		return highlightUsername;
	}
	public void setHighlightUsername(String highlightUsername) {
		this.highlightUsername = highlightUsername;
	}
	public String getHighlightPhone() {
		return highlightPhone;
	}
	public void setHighlightPhone(String highlightPhone) {
		this.highlightPhone = highlightPhone;
	}
	public String getHighlightEmail() {
		return highlightEmail;
	}
	public void setHighlightEmail(String highlightEmail) {
		this.highlightEmail = highlightEmail;
	}
	@Override
	public String toString() {
		return "SysAccountInfoVO [id=" + id + ", uid=" + uid + ", developerId=" + developerId + ", faceUri=" + faceUri
				+ ", nickname=" + nickname + ", username=" + username + ", state=" + state + ", type=" + type
				+ ", integral=" + integral + ", gender=" + gender + ", birthday=" + birthday + ", birthdayDesc="
				+ birthdayDesc + ", phone=" + phone + ", email=" + email + ", address=" + address + ", remark=" + remark
				+ ", roleIdList=" + roleIdList + ", highlightNickname=" + highlightNickname + ", highlightUsername="
				+ highlightUsername + ", highlightPhone=" + highlightPhone + ", highlightEmail=" + highlightEmail + "]";
	}
}
