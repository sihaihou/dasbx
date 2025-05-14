package com.reyco.dasbx.login.core.model.msg;

import com.reyco.dasbx.commons.utils.serializable.ToString;
import com.reyco.dasbx.rabbitmq.model.RabbitMessage;

public class AccountRegisterMessage extends ToString implements RabbitMessage{
	/**
	 * 
	 */
	private static long serialVersionUID = 2023364146932595189L;
	private String uid;
	private String email;
	private String nickname;
	private Byte gender;
	private Byte type;
	@Override
	public String getCorrelationDataId() {
		return uid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
}
