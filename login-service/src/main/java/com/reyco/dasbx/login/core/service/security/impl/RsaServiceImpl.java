package com.reyco.dasbx.login.core.service.security.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.encrypt.RSAUtils;
import com.reyco.dasbx.commons.utils.net.RequestUtils;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.login.core.service.security.RsaService;
import com.reyco.dasbx.model.constants.CachePrefixConstants;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;

@Service
public class RsaServiceImpl implements RsaService{
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public String getPublicKey() throws Exception {
		Map<String, String> publicKeyAndPrivateKeyMap = RSAUtils.getPublicKeyAndPrivateKey();
		String publicKey = publicKeyAndPrivateKeyMap.get(RSAUtils.PUBLIC_KEY_NAME);
		String privateKey = publicKeyAndPrivateKeyMap.get(RSAUtils.PRIVATE_KEY_NAME);
		String key = getKey(publicKey);
		redisUtil.set(key, privateKey, 5, TimeUnit.MINUTES);
		return publicKey;
	}

	@Override
	public String getPrivateKey(String publicKey) {
		String key = getKey(publicKey);
		String privateKey = redisUtil.get(key);
		if(StringUtils.isBlank(privateKey)) {
			throw new RuntimeException("公钥无效或已过期，请重新获取.");
		}
		return privateKey;
	}

	@Override
	public void deletePrivateKey(String publicKey) throws Exception {
		String key = getKey(publicKey);
		redisUtil.delete(key);
	}
	private String getKey(String publicKey) {
		String key = CachePrefixConstants.RSA_PRIVATE+publicKey;
		return key;
	}
}
