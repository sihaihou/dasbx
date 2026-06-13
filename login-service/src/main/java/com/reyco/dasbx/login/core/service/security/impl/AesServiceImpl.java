package com.reyco.dasbx.login.core.service.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.encrypt.AESUtils;
import com.reyco.dasbx.login.core.service.security.AesService;
import com.reyco.dasbx.redis.auto.configuration.RedisUtil;

@Service
public class AesServiceImpl implements AesService {

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public String getSecret() {
		String secret = AESUtils.createSecret();
		return secret;
	}

}
