package com.reyco.dasbx.gateway.core.sign;

public abstract class AbstractSignature implements Signature{

	@Override
	public String sign(SigningContent content) throws SignatureException{
        if (content.getSecret() == null) throw new SignatureException("Secret不能为空");
		return doSign(content);
	}

	protected abstract String doSign(SigningContent content) throws SignatureException;

}
