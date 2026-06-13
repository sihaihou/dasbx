package com.reyco.dasbx.gateway.core.sign;

public class SigningContent {
	// --- 安全要素 ---
    private String secret;      // 密钥 (仅存在于后端，不传输)
    private String timestamp;   // dasbx-Timestamp (秒级时间戳)
    private String nonce;       // dasbx-Nonce (随机字符串)
    
    // --- 请求元数据 ---
    private String method;      // 请求方式 (GET/POST等)
    private String url;        // url
    private String body;        // 请求体 (前端md5过的参数)
    
    // --- 算法配置 ---
    private String algorithm = SimpleHash.MD5; 
    
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
}
