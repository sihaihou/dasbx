package com.reyco.dasbx.commons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 盐相当于加在source前面，例：
 * 	source: 123456
 *  salt: subixin
 *  相当于md5(subixin123456)
 * @author reyco
 *
 */
public class SimpleHash {
	public static final String SHA_256 = "SHA-256";
	public static final String MD5 = "MD5";
	
	private static final int DEFAULT_ITERATIONS = 1;

	private static char[] DIGITS = { 
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8','9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private final String algorithmName;
	private byte[] bytes;
	private byte[] salt;
	private int iterations;

	public SimpleHash(String algorithmName) {
		this.algorithmName = algorithmName;
		this.iterations = DEFAULT_ITERATIONS;
	}

	public SimpleHash(String algorithmName, String source) {
		this(algorithmName, source, null);
	}

	public SimpleHash(String algorithmName, String source, String salt) {
		this(algorithmName, source, salt, DEFAULT_ITERATIONS);
	}

	public SimpleHash(String algorithmName, String source, String salt, int hashIterations) {
		this.algorithmName = algorithmName;
		this.iterations = Math.max(DEFAULT_ITERATIONS, hashIterations);
		byte[] saltBytes = null;
		if (salt != null) {
			saltBytes = salt.getBytes();
			this.salt = saltBytes;
		}
		try {
			hash(source.getBytes(), saltBytes, hashIterations);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void hash(byte[] bytes, byte[] salt, int hashIterations) throws NoSuchAlgorithmException {
		MessageDigest digest = getDigest(getAlgorithmName());
		if (salt != null) {
			digest.reset();
			digest.update(salt);
		}
		byte[] hashed = digest.digest(bytes);
		int iterations = hashIterations - 1;
		for (int i = 0; i < iterations; i++) {
			digest.reset();
			hashed = digest.digest(hashed);
		}
		setBytes(hashed);
	}

	protected MessageDigest getDigest(String algorithmName) throws NoSuchAlgorithmException {
		try {
			return MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			String msg = "No native '" + algorithmName + "' MessageDigest instance available on the current JVM.";
			throw new NoSuchAlgorithmException(msg, e);
		}
	}

	public String toHex() {
		return encodeToString(getBytes());
	}

	private static String encodeToString(byte[] bytes) {
		char[] encodedChars = encode(bytes);
		return new String(encodedChars);
	}

	private static char[] encode(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public String getAlgorithmName() {
		return algorithmName;
	}
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	public byte[] getSalt() {
		return salt;
	}
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	public int getIterations() {
		return iterations;
	}
	
	
	public static void main(String[] args) {
		String salt = "subixin";
		String source = "123456";
		String hex1 = new SimpleHash("MD5", source, salt, 1).toHex();
		System.out.println(hex1);
		String hex2 = new SimpleHash("MD5", salt+source,"" , 1).toHex();
		System.out.println(hex2);
	}
}
