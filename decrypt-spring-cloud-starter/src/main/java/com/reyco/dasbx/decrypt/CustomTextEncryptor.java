package com.reyco.dasbx.decrypt;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.reyco.dasbx.commons.utils.encrypt.AESUtils;
import com.reyco.dasbx.commons.utils.encrypt.RSAUtils;

public class CustomTextEncryptor implements TextEncryptor {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomTextEncryptor.class);
	
	public final static String AES_ALGORITHM_NAME = "AES";
	public final static String RSA_ALGORITHM_NAME = "RSA";
	public final static String DEFAULT_ALGORITHM_NAME = AES_ALGORITHM_NAME;
	public final static String DEFAULT_SECRET = "DL8H2KsOGh6dYvnge3em2nGY529ZS7mKjAnY1isGBqA="; 
	public static final String DEFAULT_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAljb6b2Ag57wpG2OozI7PFizFmG7X0vXAH4hXPPPrd+GLcHxVFiWw1M/qxpyVHF5NZwL6OO1+PKXG9SA85lfDcvPXN0oF2jrvdY/8pEdYFn8iRQLi6GCKKjkKR8qR9G8iijz+zxn+rmtGI1qzoSgnBH0NyQz9i6nQ52NUNwzPxAzRmHpwTugCSXUxmkaibm75nIByTRaEWnPn7OTtTVgKgsO5U7V8vZdrOId/9fOOF971LIlNnBJhRIKXFMgXJjqSHRHSfRWkYOJwGfzlneObPoAsPLUmfnTsMa0MryTyuZTITRwOvIy8nDLRdDdwBw9/nK3jxCHQ2AVBQQxdQiIRYQIDAQAB";
	public static final String DEFAULT_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCWNvpvYCDnvCkbY6jMjs8WLMWYbtfS9cAfiFc88+t34YtwfFUWJbDUz+rGnJUcXk1nAvo47X48pcb1IDzmV8Ny89c3SgXaOu91j/ykR1gWfyJFAuLoYIoqOQpHypH0byKKPP7PGf6ua0YjWrOhKCcEfQ3JDP2LqdDnY1Q3DM/EDNGYenBO6AJJdTGaRqJubvmcgHJNFoRac+fs5O1NWAqCw7lTtXy9l2s4h3/1844X3vUsiU2cEmFEgpcUyBcmOpIdEdJ9FaRg4nAZ/OWd45s+gCw8tSZ+dOwxrQyvJPK5lMhNHA68jLycMtF0N3AHD3+crePEIdDYBUFBDF1CIhFhAgMBAAECggEAeFACl/AritAXMJvK2ERNs1oazXOCI7K3tcnS7bwEThl1QJwk3j9D+b+qMHe7qmQRpL8qwSvfHx48U5sFjAyhO9H3/42nUFpB7yxrUvzdne8n+JROP4cLQrQ6+rNR4bCdvpEKGcCdTwAWxjxggWSSk7LBttVTwmg6LQpOqmT1h/TtijHmb3ptagiPOEaJggzNOrwtlkeQwfdxc5HX/7QmCUhccTxfpw4yjM6JLhz9uLCntDWVUjz3Ihlz6RtVuRDAwil1IWhNAmM16NW/qX0lEg/a9/UmODj0egWsiOiw1SnFcRZSJbjv/wl0j0RQu1/0gvuKGfQ8WaKCXq3F/I5S3QKBgQDSQOkCy5PAelCX+ztD8aEVSplt5o1jPA8LhvtM5W50tBANe7mURzgyjufVsCD+Yx/5T7VfZhMUCQui/MiaKKveerVL4IKoN7qrAGndMZw0ogcsfjT15gYxr9Q3jci/JEvfjc0m/fjexd55sxemtrAtWRsyoR04aHd0bwA11mZJEwKBgQC25eqlIPcNgDQbmEa6euGWvQs3wN4BC3hbdE7isRislL8GvSHLGIEq2JCNIRy/Ib2pBagr1oDyJMxa5NO4/tU4L/XANb/svbpfNDbeivOTnsti7xwHiy+Ik4pFS1DtVI3QUGfiXWhFrdLUl859wXUeCUNWYfbOfkw/eT2ySuUeOwKBgFMxtAGTJCepPnKkylKcLa0LE6LU82UswpdBFfbbYdVz2lY7ercdZzM6MKnxQ1zlRCWWUKIq9amb2qrnc0jshvVFgK9BavG8+3Pmef04QCLjagtE9RfHqPPIKyTASYq4hf0zUtX28J7VlK57GwSwjQ2kkIp8Dd/gFkRQF7k12MtZAoGAefnNykC44rUci0a77MPkzP3YjF9A75txsjRYI9EN7pLo7xqVffgitKtng+eQX1umPCH8rPfKWTL3Yqr3FIpHMsixcXWnQMCrmbCd4z7yAQJHC/0BSXqmwcP71AjNuOnKBiKJlarLzqSFVajEWpP5goWU9fgxfDc4rKAz6EMQmF8CgYEAsVh5jwxO9071WQsv1beiP0L3TgtDu1iosCiqXUSZTYnzdYLSlWNHzgbLpk89IwJN3WKXc1KozVYHaE3k9HUrItkGWLCIYmUp2MmSAlyuz1dI1hG3eP/lvnt86AN5905MZjB/UaNdq0j9MVXudlv77t4DnmlleeymvWZFdzX4MZc=";
	
	public String algorithmName;
	
	public String secret;
	
	public String publicKey;
	
	public String privateKey;
	
	public CustomTextEncryptor() {
		this(DEFAULT_ALGORITHM_NAME,DEFAULT_SECRET);
	}
	
	public CustomTextEncryptor(String algorithmName, String secret) {
		super();
		this.algorithmName = algorithmName;
		this.secret = secret;
	}
	
	public CustomTextEncryptor(String algorithmName, String publicKey, String privateKey) {
		super();
		this.algorithmName = algorithmName;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public CustomTextEncryptor(String algorithmName,String secret,String publicKey,String privateKey) {
		this.algorithmName = algorithmName;
		this.secret = secret;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	@Override
	public String encrypt(String text) {
		if (StringUtils.isBlank(text)) return text;
		
		try {
	        switch (this.algorithmName) {
	            case AES_ALGORITHM_NAME:
	                return AESUtils.encrypt(text, secret);
	            case RSA_ALGORITHM_NAME:
	                return RSAUtils.encrypt(text, publicKey);
	            default:
	                throw new IllegalArgumentException("Unsupported algorithmName: " + algorithmName);
	        }
	    } catch (Exception e) {
	        logger.error("加密失败: {}", e.getMessage());
	        throw new RuntimeException("Encryption failed", e);
	    }
	}

	@Override
	public String decrypt(String encryptedText) {
		// 1. 快速失败/透传逻辑
	    if (StringUtils.isBlank(encryptedText)) {
	        return encryptedText;
	    }
	    try {
	        String result;
	        switch (this.algorithmName) {
	            case AES_ALGORITHM_NAME:
	                result = AESUtils.decrypt(encryptedText, secret);
	                break;
	            case RSA_ALGORITHM_NAME:
	                result = RSAUtils.decrypt(encryptedText, privateKey);
	                break;
	            default:
	                throw new IllegalArgumentException("Unsupported algorithmName: " + algorithmName);
	        }
	        
	        if (logger.isDebugEnabled()) {
	            logger.debug("解密成功: {} -> {}", encryptedText, result);
	        }
	        return result;
	    } catch (Exception e) {
	        logger.error("解密异常 [{}]: {}", algorithmName, e.getMessage());
	        throw new RuntimeException("Decryption failed", e);
	    }
	}

	public static void main(String[] args) {
		String source = "123456";
		CustomTextEncryptor dasbxTextEncryptor = new CustomTextEncryptor();
		String decrypt = dasbxTextEncryptor.encrypt(source);
		System.out.println(source + "\t  cipher{" + decrypt + "} \t");
	}
}
