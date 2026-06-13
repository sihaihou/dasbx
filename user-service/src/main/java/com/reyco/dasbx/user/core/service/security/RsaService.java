package com.reyco.dasbx.user.core.service.security;

public interface RsaService {
	
	String getPublicKey() throws Exception ;
	
	String getPrivateKey(String publicKey) throws Exception ;
	
	void deletePrivateKey(String publicKey) throws Exception ;
}
