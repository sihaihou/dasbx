package com.reyco.dasbx.login.core.service.authentication;

/**
 * 
 * @author reyco
 *
 */
public class SaltAuthenticationInfo implements AuthenticationInfo {
	
	private Object principal;
	private Object credentials;
	private String algorithmName;
	private String salt;
	private int hashIterations;
	
	public SaltAuthenticationInfo() {
	}
	public SaltAuthenticationInfo(Object principal,Object credentials) {
		this(principal, credentials,"MD5", null);
	}
	public SaltAuthenticationInfo(Object principal,Object credentials,String algorithmName,String salt) {
		this(principal, credentials,algorithmName, null, 1);
	}
	public SaltAuthenticationInfo(Object principal,Object credentials,String algorithmName,String salt,int hashIterations) {
		super();
		this.principal = principal;
		this.credentials = credentials;
		this.algorithmName = algorithmName;
		this.salt = salt;
		this.hashIterations = hashIterations;
	}
	@Override
	public Object getCredentials() {
		return credentials;
	}
	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}
	@Override
	public Object getPrincipal() {
		return principal;
	}
	public void setPrincipal(Object principal) {
		this.principal = principal;
	}
	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	public String getAlgorithmName() {
		return algorithmName;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public int getHashIterations() {
		return hashIterations;
	}
	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}
}
