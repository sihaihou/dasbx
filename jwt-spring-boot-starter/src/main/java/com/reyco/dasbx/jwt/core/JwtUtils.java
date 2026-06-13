package com.reyco.dasbx.jwt.core;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.InitializingBean;

import com.reyco.dasbx.jwt.properties.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwsHeader;

public class JwtUtils implements InitializingBean{
	
	private JwtProperties jwtProperties;
	
	private SecretKey cachedKey;
	
	public JwtUtils() {
	}
	
	public JwtUtils(JwtProperties jwtProperties) {
		super();
		this.jwtProperties = jwtProperties;
	}
	public JwtProperties getJwtProperties() {
		return jwtProperties;
	}
	public void setJwtProperties(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		SignatureAlgorithm.forName(jwtProperties.getAlgorithmName());
		byte[] encodedKey = Base64.getDecoder().decode(jwtProperties.getJwtSecret());
		this.cachedKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
	}
	/**
	 * 签发JWT，创建token的方法。
	 * @param uniqueId 唯一标识：jwt所面向的用户的唯一标识,payload中记录的public claims。当前环境中就是用户的登录名。
	 * @subject 主题：jwt所面向的用户信息
	 * @return token， token是一次性的。是为一个用户的有效登录周期准备的一个token。用户退出或超时，token失效。
	 * @throws Exception
	 */
	public String createToken(String uniqueId,String subject) {
		return createToken(uniqueId,subject,null,null,null,null);
	}
	/**
	 * 签发JWT，创建token的方法。
	 * @param uniqueId 唯一标识：jwt所面向的用户的唯一标识,payload中记录的public claims。当前环境中就是用户的登录名。
	 * @param subject 主题：jwt所面向的用户信息
	 * @param claims 附加信息
	 * @param header 头信息
	 * @param issuer 签发者
	 * @param audience 观众
	 * @return token， token是一次性的。是为一个用户的有效登录周期准备的一个token。用户退出或超时，token失效。
	 * @throws Exception
	 */
	public String createToken(String uniqueId,String subject,Map<String,Object> claims,Map<String,Object> header,String issuer,String audience) {
		Date now = new Date();
		Date expireDate = new Date(now.getTime() + 1000*jwtProperties.getExpires());
		if(claims==null) {
			claims = new HashMap<String,Object>();
		}
		if(header==null) {
			header = new DefaultJwsHeader();
		}
		try {
			return Jwts.builder().setClaims(claims)//附加信息
					.setHeader(header)// 头信息
					.setAudience(audience) //audience
					.setId(uniqueId)// 唯一标识
					.setIssuedAt(now)// 签发时间
					.setIssuer(issuer)// 签发者
					.signWith(SignatureAlgorithm.forName(jwtProperties.getAlgorithmName()), cachedKey)// 设定密匙和算法
					.setSubject(subject)// 所要的数据
					.setExpiration(expireDate)// 过期时间
					.compact();// 生成token
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成Token失败,e:{}"+e.getMessage());
		}
	}
	/**
	 * 解析token字符串
	 * @param token 就是服务器为客户端生成的签名数据，就是token。
	 * @return
	 * @throws Exception
	 */
	public Jws<Claims> parseToken(String token) {
		try {
			return Jwts.parser().setSigningKey(cachedKey).parseClaimsJws(token);
		} catch (Exception e) {
			throw new RuntimeException("解析Token失败,e:{}"+e.getMessage());
		}
	}
	/**
	 * 验证token
	 * @param token
	 * @return
	 */
	public boolean verifyToken(String token) {
		return parseToken(token)!=null;
	}
	/**
	 * 获取附加信息
	 * @param Claims
	 * @return
	 */
	public Claims getClaims(String token) {
		Jws<Claims> jws = parseToken(token);
		return jws != null ? jws.getBody() : null;
	}
	/**
	 * 获取主题信息
	 * @param token
	 * @return
	 */
	public String getSubject(String token) {
		Claims claims = getClaims(token);
		return claims != null ? claims.getSubject() : null;
	}
	/**
	 * 获取头信息(header)
	 * @param Header
	 * @return
	 */
	public JwsHeader<?> getHeader(String token) {
		Jws<Claims> jws = parseToken(token);
		return jws != null ? jws.getHeader() : null;
	}
	/**
	 * 获取签发者信息
	 * @param Header
	 * @return
	 */
	public String getIssuer(String token) {
		Claims claims = getClaims(token);
		return claims != null ? claims.getIssuer() : null;
	}
	/**
	 * 获取Audience
	 * @param Header
	 * @return
	 */
	public String getAudience(String token) {
		Claims claims = getClaims(token);
		return claims != null ? claims.getAudience() : null;
	}
	
	/**
	 * 获取Jws信息
	 * @param Claims
	 * @return
	 */
	public Jws<Claims> getJws(String token) {
		return Jwts.parser().setSigningKey(cachedKey).parseClaimsJws(token);
	}
	/**
	 * 获取签名信息
	 * @param Claims
	 * @return
	 */
	public String getSignature(String token) {
		Jws<Claims> jws = getJws(token);
		return jws.getSignature();
	}
	/**
	 * 刷新token
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public String refreshToken(String token){
		try {
			Jws<Claims> jws = parseToken(token);
			if (jws == null) {
				return null;
			}
			JwsHeader header = jws.getHeader();
			Claims claims = jws.getBody();
			return createToken(claims.getId(),claims.getSubject(),claims,header,claims.getIssuer(),claims.getAudience());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
