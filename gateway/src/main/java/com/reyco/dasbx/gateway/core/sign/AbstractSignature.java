package com.reyco.dasbx.gateway.core.sign;

public abstract class AbstractSignature implements Signature{

	@Override
	public String getSign(String algorithmName, String source, String salt, int hashIterations) throws SignatureException{
		return doGetSign(algorithmName,source,salt,hashIterations);
	}

	protected abstract String doGetSign(String algorithmName, String source, String salt, int hashIterations) throws SignatureException;

}
