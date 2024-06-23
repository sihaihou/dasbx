package com.reyco.dasbx.user.core.model.vo.sys;

import com.reyco.dasbx.commons.utils.Dasbx;
import com.reyco.dasbx.model.vo.ListVO;

public class SysAccountListVO implements ListVO {
	private Long id;
	private String uid;
	private String nickname;
	private String username;
	private Integer type;
	private Integer integral;
	private Byte gender;
	private Long birthday;
	private String birthdayDesc;
	private String phone;
	private String email;
	private String address;
	private Byte state;

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
		if (this.birthday != null) {
			this.birthdayDesc = Dasbx.getDateByTimeZone(birthday, Dasbx.FORMAT_YYYY_MM_DD);
		}
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

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}
}
