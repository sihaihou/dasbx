package com.reyco.dasbx.commons.utils.encrypt;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

/**
 * AES加密工具类
 * 
 * @author reyco
 *
 */
public class AESUtils {
	/**
	 * 日志相关
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AESUtils.class);
	/**
	 * 编码
	 */
	private static final String ENCODING = "UTF-8";
	/**
	 * 算法定义
	 */
	private static final String AES_ALGORITHM = "AES";
	/**
	 * 指定填充方式
	 */
	private static final String CIPHER_PADDING = "AES/ECB/PKCS5Padding";
	private static final String CIPHER_CBC_PADDING = "AES/CBC/PKCS5Padding";
	
	private static final int IV_LENTH = 16;

	/**
	 * AES加密
	 * 
	 * @param content 待加密内容
	 * @param aesKey Base64格式的密钥
	 * @return Base64格式密文
	 */
	public static String encrypt(String content, String aesKey) {
		if (StringUtils.isBlank(content)) {
			LOGGER.info("AES encrypt: the content is null!");
			return null;
		}
		try {
			 // 解码Base64得到32字节密钥
			byte[] realKey = Base64.getDecoder().decode(aesKey);
			// 设置加密算法，生成秘钥
			SecretKeySpec keySpec = new SecretKeySpec(realKey, AES_ALGORITHM);
			
			// "算法/模式/补码方式"
			Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
			// 选择加密
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			// 根据待加密内容生成字节数组
			byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
			
			// 返回base64字符串
			return Base64Utils.encodeToString(encrypted);
		} catch (Exception e) {
			LOGGER.info("AES encrypt exception:",e.getMessage(),e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解密
	 * 
	 * @param content Base64格式的密文
	 * @param aesKey Base64格式的密钥
	 * @return 明文字符串
	 */
	public static String decrypt(String content, String aesKey) {
		if (StringUtils.isBlank(content)) {
			LOGGER.info("AES decrypt: the content is null!");
			return null;
		}
		try {
			// 对密码进行编码
			byte[] realKey = Base64.getDecoder().decode(aesKey);
			// 设置解密算法，生成秘钥
			SecretKeySpec keySpec = new SecretKeySpec(realKey, AES_ALGORITHM);
			// "算法/模式/补码方式"
			Cipher cipher = Cipher.getInstance(CIPHER_PADDING);
			// 选择解密
			cipher.init(Cipher.DECRYPT_MODE, keySpec);

			// 先进行Base64解码
			byte[] decodeBase64 = Base64Utils.decodeFromString(content);

			// 根据待解密内容进行解密
			byte[] decrypted = cipher.doFinal(decodeBase64);
			// 将字节数组转成字符串
			return new String(decrypted, ENCODING);
		} catch (Exception e) {
			LOGGER.info("AES decrypt exception:" + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * AES_CBC加密
	 * 
	 * @param content 待加密内容
	 * @param aesKey Base64格式的密钥
	 * @return Base64格式密文
	 */
	public static String encryptCBC(String content, String aesKey) {
		if (StringUtils.isBlank(content)) {
			LOGGER.info("AES_CBC encrypt: the content is null!");
			return null;
		}
		try {
			// 对密码进行编码
			byte[] realKey = Base64.getDecoder().decode(aesKey);
			// 设置加密算法，生成秘钥
			SecretKeySpec skeySpec = new SecretKeySpec(realKey, AES_ALGORITHM);
			
			// 偏移
			byte[] iv = new byte[IV_LENTH];
			new SecureRandom().nextBytes(iv);
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			
			// "算法/模式/补码方式"
			Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
			// 选择加密
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
			
			// 根据待加密内容生成字节数组
			byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
			
			byte[] combined = new byte[IV_LENTH+encrypted.length];
			System.arraycopy(iv, 0, combined, 0, IV_LENTH);
			System.arraycopy(encrypted, 0, combined, IV_LENTH, encrypted.length);
			
			// 返回base64字符串
			return Base64Utils.encodeToString(combined);
		} catch (Exception e) {
			LOGGER.info("AES_CBC encrypt exception:" + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	/**
	 * AES_CBC解密
	 * 
	 * @param content Base64格式的密文
	 * @param aesKey Base64格式的密钥
	 * @return 明文字符串
	 */
	public static String decryptCBC(String content, String aesKey) {
		if (StringUtils.isBlank(content)) {
			LOGGER.info("AES_CBC decrypt: the content is null!");
			return null;
		}
		try {
			
			byte[] combined = Base64Utils.decodeFromString(content);
			byte[] iv = new byte[IV_LENTH];
			byte[] decryptedData = new byte[combined.length-IV_LENTH];
			System.arraycopy(combined, 0, iv, 0, IV_LENTH);
			System.arraycopy(combined, IV_LENTH, decryptedData, 0, decryptedData.length);
			
			// 对密码进行编码
			byte[] realKey = Base64.getDecoder().decode(aesKey);
						
			// 设置解密算法，生成秘钥
			SecretKeySpec keySpec = new SecretKeySpec(realKey, AES_ALGORITHM);
			// 偏移
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			
			// "算法/模式/补码方式"
			Cipher cipher = Cipher.getInstance(CIPHER_CBC_PADDING);
			// 选择解密
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

			// 根据待解密内容进行解密
			byte[] decrypted = cipher.doFinal(decryptedData);
			// 将字节数组转成字符串
			return new String(decrypted, ENCODING);
		} catch (Exception e) {
			LOGGER.info("AES_CBC decrypt exception:" + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	/**
	 * 创建密钥
	 * @return
	 */
	public static String createSecret() {
		byte[] key = new byte[32];
	    new SecureRandom().nextBytes(key);
	    String secret = Base64.getEncoder().encodeToString(key);
	    return secret;
	}

	public static void main(String[] args) {
		// AES支持三种长度的密钥：128位、192位、256位。
		// 代码中这种就是128位的加密密钥，16字节 * 8位/字节 = 128位。
		//String source = "{\"username\":\"admin\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\"}";
		//String source = "Hello World";
		String source = "admin";
		String random = "DL8H2KsOGh6dYvnge3em2nGY529ZS7mKjAnY1isGBqA=";
		System.out.println("随机key:" + random);
		System.out.println();

		System.out.println("---------加密---------");
		String aesResult = encrypt(source, random);
		System.out.println("aes加密结果:" + aesResult);
		System.out.println();

		System.out.println("---------解密---------");
		String decrypt = decrypt(aesResult, random);
		System.out.println("aes解密结果:" + decrypt);
		System.out.println();

		System.out.println("--------AES_CBC加密解密---------");
		String cbcResult = encryptCBC(source, random);
		System.out.println("aes_cbc加密结果:" + cbcResult);
		System.out.println();

		System.out.println("---------解密CBC---------");
		String cbcDecrypt = decryptCBC(cbcResult, random);
		System.out.println("aes解密结果:" + cbcDecrypt);
		System.out.println();
	}
}
