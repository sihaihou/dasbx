package com.reyco.dasbx.jwt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.jsonwebtoken.SignatureAlgorithm;

@ConfigurationProperties(JwtProperties.JWT_PREFIX)
public class JwtProperties{
	
	public final static String JWT_PREFIX = "reyco.dasbx.jwt";
	
	private final static Long DEFALUT_EXPIRE_TIME = 1800L;
	
	/**
	 * 密匙
	 */
	private String jwtSecert = "U2tqk9Pk27vdRnWh9mqj5i26i6vG5N69nWmsp4I3G41DSX02BMqhRMwgc6CJtgiV";
	
	/**
	 * 算法名称 ，@See SignatureAlgorithm
	 */
	private String algorithmName = SignatureAlgorithm.HS256.getValue();
	
	/**
	 * 过期时间：单位/second
	 */
	private Long expires = DEFALUT_EXPIRE_TIME;
	
	public String getJwtSecert() {
		return jwtSecert;
	}
	public void setJwtSecert(String jwtSecert) {
		this.jwtSecert = jwtSecert;
	}
	public String getAlgorithmName() {
		return algorithmName;
	}
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	public Long getExpires() {
		return expires;
	}
	public void setExpires(Long expires) {
		this.expires = expires;
	}
	
}
