package com.reyco.dasbx.gateway.core.sign;

public class DefaultSignature extends AbstractSignature{

	@Override
	protected String doGetSign(String algorithmName, String source, String salt, int hashIterations) throws SignatureException{
		return new SimpleHash(algorithmName, source, salt, hashIterations).toHex();
	}

}
