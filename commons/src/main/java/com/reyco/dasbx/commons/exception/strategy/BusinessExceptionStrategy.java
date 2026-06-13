package com.reyco.dasbx.commons.exception.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.commons.exception.BusinessException;
import com.reyco.dasbx.commons.exception.DasbxException;
import com.reyco.dasbx.commons.exception.ExceptionCode;

public class BusinessExceptionStrategy implements ExceptionStrategy {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean support(String type) {
		return type.equals(ExceptionCode.BUSINESS_EXCEPTION.getType());
	}

	@Override
	public R getExceptionMessage(DasbxException e) {
		BusinessException businessException = (BusinessException) e;
		logger.error("业务异常：" + businessException.getMsg());
		R r = R.fail(businessException.getCode(),null,"业务异常,code:" + businessException.getCode() + ",msg:" + businessException.getMsg(),businessException.getMsg());
		return r;
	}

}
