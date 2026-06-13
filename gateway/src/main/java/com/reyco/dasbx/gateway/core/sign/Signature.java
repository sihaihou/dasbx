package com.reyco.dasbx.gateway.core.sign;

public interface Signature {

	String sign(SigningContent content) throws SignatureException;
	
}
