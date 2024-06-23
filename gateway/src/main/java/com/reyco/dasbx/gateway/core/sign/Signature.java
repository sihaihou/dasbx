package com.reyco.dasbx.gateway.core.sign;

public interface Signature {

	String getSign(String algorithmName,String source,String salt,int hashIterations) throws SignatureException;
	
}
