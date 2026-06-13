package com.reyco.dasbx.login.core.service.security;

public interface RsaService {
	
	String getPublicKey() throws Exception ;
	
	String getPrivateKey(String publicKey) throws Exception ;
	
	void deletePrivateKey(String publicKey) throws Exception ;

}
