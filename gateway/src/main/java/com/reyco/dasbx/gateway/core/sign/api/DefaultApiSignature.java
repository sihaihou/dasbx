package com.reyco.dasbx.gateway.core.sign.api;

import org.apache.commons.lang3.StringUtils;

import com.reyco.dasbx.gateway.core.sign.DefaultSignature;
import com.reyco.dasbx.gateway.core.sign.Signature;
import com.reyco.dasbx.gateway.core.sign.SignatureException;
import com.reyco.dasbx.gateway.core.sign.SigningContent;
import com.reyco.dasbx.gateway.core.sign.SimpleHash;

public class DefaultApiSignature extends AbstractApiSignature{
	
	private Signature signature = new DefaultSignature();
	
	private SecretService secretService = new DefaultSecretService();
	
	public Signature getSignature() {
		return signature;
	}
	public void setSignature(Signature signature) {
		this.signature = signature;
	}
	public SecretService getSecretService() {
		return secretService;
	}
	public void setSecretService(SecretService secretService) {
		this.secretService = secretService;
	}
	@Override
	protected String doSign(SigningContent context) throws SignatureException {
		return signature.sign(context);
	}
	
	@Override
	protected String getSignAlgorithmName(String type) throws SignatureException {
		return type.hashCode()%2==0 ? SimpleHash.MD5 : SimpleHash.SHA_256;
	}
	@Override
	protected String getSecret(String appId) throws SignatureException {
		//密匙
    	String secret = secretService.getSecret(appId);
        //验证 密钥 是否合法
        if(StringUtils.isBlank(secret)){
            log.debug("获取签名密钥失败 appId:{}",appId);
            throw new SignatureException(GateWayResultCode.DASBX_APPID.getMsg());
        }
		return secret;
	}
}
