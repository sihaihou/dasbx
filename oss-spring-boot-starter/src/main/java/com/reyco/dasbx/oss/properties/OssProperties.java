package com.reyco.dasbx.oss.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value=OssProperties.OSS_PREFIX)
public class OssProperties {
	
	public final static String OSS_PREFIX = "reyco.dasbx.oss";
	
	/**
	 *  存储类型：local（本地）、aliyun（阿里云）、tencent（腾讯云）、s3（亚马逊或MinIO）
	 *  警告(目前只支持local、aliyun)
	 */
	private String type = "local";
	
	private String basePath;
	private String baseUrl;
	
	/**
	 * 阿里云 OSS 专属配置
	 */
	private Aliyun aliyun = new Aliyun();
	/**
	 * 腾讯云 COS 专属配置
	 */
	private Tencent tencent = new Tencent();
	/**
	 * 标准 S3 协议配置（适用于 AWS S3、MinIO、Ceph等）
	 */
	private S3 s3 = new S3();
	

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public Aliyun getAliyun() {
		return aliyun;
	}
	public void setAliyun(Aliyun aliyun) {
		this.aliyun = aliyun;
	}
	public Tencent getTencent() {
		return tencent;
	}
	public void setTencent(Tencent tencent) {
		this.tencent = tencent;
	}
	public S3 getS3() {
		return s3;
	}
	public void setS3(S3 s3) {
		this.s3 = s3;
	}

	public class Aliyun {
		private String endpoint;
		private String accessKeyId;
		private String accessKeySecret;
		private String bucketName;
		public String getEndpoint() {
			return endpoint;
		}
		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}
		public String getAccessKeyId() {
			return accessKeyId;
		}
		public void setAccessKeyId(String accessKeyId) {
			this.accessKeyId = accessKeyId;
		}
		public String getAccessKeySecret() {
			return accessKeySecret;
		}
		public void setAccessKeySecret(String accessKeySecret) {
			this.accessKeySecret = accessKeySecret;
		}
		public String getBucketName() {
			return bucketName;
		}
		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}
	}
	public class Tencent {
		private String region; // 腾讯云专属地域，如 ap-shanghai
		private String secretId;
		private String secretKey;
		private String bucketName;
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getSecretId() {
			return secretId;
		}
		public void setSecretId(String secretId) {
			this.secretId = secretId;
		}
		public String getSecretKey() {
			return secretKey;
		}
		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}
		public String getBucketName() {
			return bucketName;
		}
		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}
	}
	public class S3 {
		private String endpoint; // MinIO 等自建协议需要
		private String region;   // 官方 S3 需要，如 us-east-1
		private String accessKey;
		private String secretKey;
		private String bucketName;
		public String getEndpoint() {
			return endpoint;
		}
		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getAccessKey() {
			return accessKey;
		}
		public void setAccessKey(String accessKey) {
			this.accessKey = accessKey;
		}
		public String getSecretKey() {
			return secretKey;
		}
		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}
		public String getBucketName() {
			return bucketName;
		}
		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}
	}
}
