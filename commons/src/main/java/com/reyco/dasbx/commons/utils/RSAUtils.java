package com.reyco.dasbx.commons.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.springframework.util.Base64Utils;

/**
 * RSA加密工具类
 */
public class RSAUtils {
	/**
	 * 加密算法AES
	 */
	private static final String ALGORITHM_NAME = "RSA";
	/**
	 * Map获取公钥的key
	 */
	private static final String PUBLIC_KEY_NAME = "publicKey";
	/**
	 * Map获取私钥的key
	 */
	private static final String PRIVATE_KEY_NAME = "privateKey";
	private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAljb6b2Ag57wpG2OozI7PFizFmG7X0vXAH4hXPPPrd+GLcHxVFiWw1M/qxpyVHF5NZwL6OO1+PKXG9SA85lfDcvPXN0oF2jrvdY/8pEdYFn8iRQLi6GCKKjkKR8qR9G8iijz+zxn+rmtGI1qzoSgnBH0NyQz9i6nQ52NUNwzPxAzRmHpwTugCSXUxmkaibm75nIByTRaEWnPn7OTtTVgKgsO5U7V8vZdrOId/9fOOF971LIlNnBJhRIKXFMgXJjqSHRHSfRWkYOJwGfzlneObPoAsPLUmfnTsMa0MryTyuZTITRwOvIy8nDLRdDdwBw9/nK3jxCHQ2AVBQQxdQiIRYQIDAQAB";
	private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCWNvpvYCDnvCkbY6jMjs8WLMWYbtfS9cAfiFc88+t34YtwfFUWJbDUz+rGnJUcXk1nAvo47X48pcb1IDzmV8Ny89c3SgXaOu91j/ykR1gWfyJFAuLoYIoqOQpHypH0byKKPP7PGf6ua0YjWrOhKCcEfQ3JDP2LqdDnY1Q3DM/EDNGYenBO6AJJdTGaRqJubvmcgHJNFoRac+fs5O1NWAqCw7lTtXy9l2s4h3/1844X3vUsiU2cEmFEgpcUyBcmOpIdEdJ9FaRg4nAZ/OWd45s+gCw8tSZ+dOwxrQyvJPK5lMhNHA68jLycMtF0N3AHD3+crePEIdDYBUFBDF1CIhFhAgMBAAECggEAeFACl/AritAXMJvK2ERNs1oazXOCI7K3tcnS7bwEThl1QJwk3j9D+b+qMHe7qmQRpL8qwSvfHx48U5sFjAyhO9H3/42nUFpB7yxrUvzdne8n+JROP4cLQrQ6+rNR4bCdvpEKGcCdTwAWxjxggWSSk7LBttVTwmg6LQpOqmT1h/TtijHmb3ptagiPOEaJggzNOrwtlkeQwfdxc5HX/7QmCUhccTxfpw4yjM6JLhz9uLCntDWVUjz3Ihlz6RtVuRDAwil1IWhNAmM16NW/qX0lEg/a9/UmODj0egWsiOiw1SnFcRZSJbjv/wl0j0RQu1/0gvuKGfQ8WaKCXq3F/I5S3QKBgQDSQOkCy5PAelCX+ztD8aEVSplt5o1jPA8LhvtM5W50tBANe7mURzgyjufVsCD+Yx/5T7VfZhMUCQui/MiaKKveerVL4IKoN7qrAGndMZw0ogcsfjT15gYxr9Q3jci/JEvfjc0m/fjexd55sxemtrAtWRsyoR04aHd0bwA11mZJEwKBgQC25eqlIPcNgDQbmEa6euGWvQs3wN4BC3hbdE7isRislL8GvSHLGIEq2JCNIRy/Ib2pBagr1oDyJMxa5NO4/tU4L/XANb/svbpfNDbeivOTnsti7xwHiy+Ik4pFS1DtVI3QUGfiXWhFrdLUl859wXUeCUNWYfbOfkw/eT2ySuUeOwKBgFMxtAGTJCepPnKkylKcLa0LE6LU82UswpdBFfbbYdVz2lY7ercdZzM6MKnxQ1zlRCWWUKIq9amb2qrnc0jshvVFgK9BavG8+3Pmef04QCLjagtE9RfHqPPIKyTASYq4hf0zUtX28J7VlK57GwSwjQ2kkIp8Dd/gFkRQF7k12MtZAoGAefnNykC44rUci0a77MPkzP3YjF9A75txsjRYI9EN7pLo7xqVffgitKtng+eQX1umPCH8rPfKWTL3Yqr3FIpHMsixcXWnQMCrmbCd4z7yAQJHC/0BSXqmwcP71AjNuOnKBiKJlarLzqSFVajEWpP5goWU9fgxfDc4rKAz6EMQmF8CgYEAsVh5jwxO9071WQsv1beiP0L3TgtDu1iosCiqXUSZTYnzdYLSlWNHzgbLpk89IwJN3WKXc1KozVYHaE3k9HUrItkGWLCIYmUp2MmSAlyuz1dI1hG3eP/lvnt86AN5905MZjB/UaNdq0j9MVXudlv77t4DnmlleeymvWZFdzX4MZc=";
	private static final int INITIALIZE_LENGTH = 2048;
	/**
	 * 后端RSA的密钥对(公钥和私钥)Map，由静态代码块赋值
	 */
	private static Map<String,Key> keyMap = new HashMap<>();
	
	static {
		try {
			KeyPair keyPair = generateKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			keyMap.put(PUBLIC_KEY_NAME, publicKey);
			keyMap.put(PRIVATE_KEY_NAME, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 公钥加密
	 * @param source
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String source, String publicKey) throws Exception {
		PublicKey p = KeyFactory.getInstance(ALGORITHM_NAME)
                .generatePublic(new X509EncodedKeySpec(Base64Utils.decodeFromString(publicKey)));
		Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
		cipher.init(Cipher.ENCRYPT_MODE, p);
		byte[] encryptedData = cipher.doFinal(source.getBytes());
		return Base64Utils.encodeToString(encryptedData);
	}
	/**
	 * 私钥解密
	 * @param encrypted
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encrypted, String privateKey) throws Exception {
        PrivateKey p = KeyFactory.getInstance(ALGORITHM_NAME)
                .generatePrivate(new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(privateKey)));
		Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
		cipher.init(Cipher.DECRYPT_MODE, p);
		byte[] encryptedByte = cipher.doFinal(Base64Utils.decodeFromString(encrypted));
		return new String(encryptedByte);
	}
	/**
	 * 获取密匙对对象
	 * @return
	 * @throws Exception
	 */
	private static KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_NAME);
		keyPairGenerator.initialize(INITIALIZE_LENGTH);
		return keyPairGenerator.generateKeyPair();
	}
	public static void main(String[] args) throws Exception {
		String source = "123456";
		//获取公钥私钥
		/*Key publickey = keyMap.get(PUBLIC_KEY_NAME);
		Key privateKey = keyMap.get(PRIVATE_KEY_NAME);
		byte[] publicKeyByte = publickey.getEncoded();
		String publicKeyStr = Base64Utils.encodeToString(publicKeyByte);
		byte[] privateKeyByte = privateKey.getEncoded();
		String privateKeyStr = Base64Utils.encodeToString(privateKeyByte);
		System.out.println("publicKey: " + publicKeyStr);
		System.out.println("privateKey: " + privateKeyStr);*/
		// 使用公钥加密
		String encryptedText = encrypt(source,PUBLIC_KEY);
		System.out.println("Encrypted Source: " + encryptedText);
		// 使用私钥解密
		String decryptedText = decrypt(encryptedText, PRIVATE_KEY);
		System.out.println("Decrypted Source: " + decryptedText);
	}
}