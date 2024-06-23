package com.reyco.dasbx.config.exception.core.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.ArgumentException;
import com.reyco.dasbx.config.exception.core.DasbxException;
import com.reyco.dasbx.config.exception.core.ExceptionCode;

/**
 * 参数异常
 * @author reyco
 *
 */
public class ArgumentExceptionStrategy implements ExceptionStrategy {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean support(String type) {
		return type.equals(ExceptionCode.ARGUMENT_EXCEPTION.getType());
	}

	@Override
	public R getExceptionMessage(DasbxException e) {
		ArgumentException argumentException = (ArgumentException) e;
		logger.error("参数异常：" + argumentException.getMsg());
		R r = R.fail(argumentException.getCode(),null,"参数异常,code:" + argumentException.getCode() + ",msg:" + argumentException.getMsg(),argumentException.getMsg());
		return r;
	}

}
