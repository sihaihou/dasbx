package com.reyco.dasbx.login.core.model.dto;

import com.reyco.dasbx.commons.utils.serializable.ToString;

public class LoginDto extends ToString {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String publicKey;
	private String encryptedAesKey;
	private String token;
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
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
