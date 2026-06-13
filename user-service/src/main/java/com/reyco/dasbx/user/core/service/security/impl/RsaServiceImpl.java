package com.reyco.dasbx.user.core.service.security.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.encrypt.RSAUtils;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;
import com.reyco.dasbx.user.core.service.security.RsaService;

@Service
public class RsaServiceImpl implements RsaService{
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public String getPublicKey() throws Exception {
		Map<String, String> publicKeyAndPrivateKey = RSAUtils.getPublicKeyAndPrivateKey();
		String publicKey = publicKeyAndPrivateKey.get(RSAUtils.PUBLIC_KEY_NAME);
		String privateKey = publicKeyAndPrivateKey.get(RSAUtils.PRIVATE_KEY_NAME);
		redisUtil.set(CachePrefixConstants.RSA_PRIVATE+publicKey, privateKey, 5, TimeUnit.MINUTES);
		return publicKey;
	}

	@Override
	public String getPrivateKey(String publicKey) {
		String privateKey = redisUtil.get(CachePrefixConstants.RSA_PRIVATE+publicKey);
		if(StringUtils.isBlank(privateKey)) {
			throw new RuntimeException("公钥无效或已过期，请重新获取.");
		}
		return privateKey;
	}

	@Override
	public void deletePrivateKey(String publicKey) throws Exception {
		redisUtil.delete(publicKey);
	}

}
