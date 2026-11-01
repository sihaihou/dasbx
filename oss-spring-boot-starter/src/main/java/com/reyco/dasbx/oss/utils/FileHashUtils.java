package com.reyco.dasbx.oss.utils;

import java.io.InputStream;
import java.security.MessageDigest;

import org.springframework.web.multipart.MultipartFile;

public class FileHashUtils {
	/**
     * 计算 MultipartFile 的 Hash 值 (默认 SHA-256)
     */
    public static String getHash(MultipartFile file) throws Exception {
        return getHash(file.getInputStream(), "SHA-256");
    }

    /**
     * 计算 Base64 的 Hash 值
     */
    public static String getBase64Hash(String base64) throws Exception {
        String base64Data = base64.split(",")[1];
        byte[] bytes = java.util.Base64.getDecoder().decode(base64Data);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(bytes);
        return bytesToHex(hashBytes);
    }

    /**
     * 流式计算 Hash（避免大文件 OOM）
     */
    public static String getHash(InputStream inputStream, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[8192];
        int bytesRead;
        try (InputStream is = inputStream) {
            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        return bytesToHex(digest.digest());
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
