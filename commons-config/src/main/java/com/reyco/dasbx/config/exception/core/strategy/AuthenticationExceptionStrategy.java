package com.reyco.dasbx.config.exception.core.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.AuthenticationException;
import com.reyco.dasbx.config.exception.core.DasbxException;
import com.reyco.dasbx.config.exception.core.ExceptionCode;

public class AuthenticationExceptionStrategy implements ExceptionStrategy{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean support(String type) {
		return type.equals(ExceptionCode.AUTHENTICATION_EXCEPTION.getType());
	}

	@Override
	public R getExceptionMessage(DasbxException e) {
		AuthenticationException authenticationException = (AuthenticationException) e;
		logger.error("认证失败：" + authenticationException.getMsg());
		R r = R.authFail(authenticationException.getCode(),null,"认证失败,code:" + authenticationException.getCode() + ",msg:" + authenticationException.getMsg(),authenticationException.getMsg());
		return r;
	}

}
