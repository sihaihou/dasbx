package com.reyco.dasbx.rate.limit.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.commons.exception.DasbxException;
import com.reyco.dasbx.commons.exception.ExceptionCode;
import com.reyco.dasbx.commons.exception.strategy.ExceptionStrategy;

public class RateLimitExceptionStrategy implements ExceptionStrategy {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean support(String type) {
		return type.equals(ExceptionCode.RATE_LIMIT_EXCEPTION.getType());
	}

	@Override
	public R getExceptionMessage(DasbxException e) {
		RateLimitException rateLimitException = (RateLimitException) e;
		logger.error("限流异常：" + rateLimitException.getMsg());
		R r = R.fail(rateLimitException.getCode(),null,"限流异常,code:" + rateLimitException.getCode() + ",msg:" + rateLimitException.getMsg(),rateLimitException.getMsg());
		return r;
	}

}

