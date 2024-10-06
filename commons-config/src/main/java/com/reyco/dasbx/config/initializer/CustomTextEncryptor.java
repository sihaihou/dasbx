package com.reyco.dasbx.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.reyco.dasbx.commons.utils.encrypt.AESUtils;

public class CustomTextEncryptor implements TextEncryptor {
	private static final Logger logger = LoggerFactory.getLogger(CustomTextEncryptor.class);
	private final static String KEY = "0123456789012345";

	@Override
	public String encrypt(String text) {
		return AESUtils.encrypt(text, KEY);
	}

	@Override
	public String decrypt(String encryptedText) {
		String decrypt = AESUtils.decrypt(encryptedText, KEY);
		logger.debug("解码前:{},解密后：{}", encryptedText, decrypt);
		return decrypt;
	}

	public static void main(String[] args) {
		String source = "123456";
		DasbxTextEncryptor dasbxTextEncryptor = new DasbxTextEncryptor();
		String decrypt = dasbxTextEncryptor.encrypt(source);
		System.out.println(source + "\t  cipher{" + decrypt + "} \t");
	}
}
