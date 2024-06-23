package com.reyco.dasbx.config.exception.core.strategy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.DasbxException;

/**
 * 异常策略委托类
 * @author reyco
 *
 */
public class DelegationExceptionStrategy implements ExceptionStrategy{
	
	@Autowired
	private List<ExceptionStrategy> exceptionStrategys;
	
	@Override
	public boolean support(String type) {
		return false;
	}
	
	@Override
	public R getExceptionMessage(DasbxException e) {
		ExceptionStrategy exceptionStrategy = getMatchExceptionStrategy(e);
		if(exceptionStrategy==null) {
			return R.fail(e.getMsg());
		}
		return exceptionStrategy.getExceptionMessage(e);
	}
	private ExceptionStrategy getMatchExceptionStrategy(DasbxException e) {
		for (ExceptionStrategy exceptionStrategy : exceptionStrategys) {
			if(exceptionStrategy.support(e.getType())) {
				return exceptionStrategy;
			}
		}
		return null;
	}
	
}
