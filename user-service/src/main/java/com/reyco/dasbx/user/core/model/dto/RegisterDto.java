package com.reyco.dasbx.user.core.model.dto;

import com.reyco.dasbx.commons.utils.serializable.ToString;

public class RegisterDto extends ToString{
	private String publicKey;
	private String encryptedAesKey;
	private String userInfo;
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getEncryptedAesKey() {
		return encryptedAesKey;
	}
	public void setEncryptedAesKey(String encryptedAesKey) {
		this.encryptedAesKey = encryptedAesKey;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
}
