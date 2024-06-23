package com.reyco.dasbx.commons.utils;

/**
 * 
 * @author reyco
 *
 */
public class PasswordUtils {
	/**
	 * 校验密码是否正确
	 * @param source	原始密码
	 * @param password	加密的密码
	 * @param salt		盐
	 * @return
	 */
	public static Boolean checkPassword(String source, String password,String salt) {
		password = getEncryptionPassword(password, salt);
		if (source.equals(password)) {
			return true;
		}
		return false;
	}
	/**
	 * 获取加密的密码
	 * @param source	原始密码
	 * @param salt		盐
	 * @return
	 */
	public static String getEncryptionPassword(String source, String salt) {
		return new SimpleHash(SimpleHash.SHA_256, source, salt, 2).toHex();
	}
	public static void main(String[] args) {
		String password = "123456";
		String salt = "peUbAblByniKkOEAbukS";
		password = new SimpleHash(SimpleHash.MD5, password,salt).toHex();
		System.out.println(password);
		System.out.println(salt);
		String encryptionPassword = getEncryptionPassword(password, salt);
		System.out.println(encryptionPassword);
	}
}
