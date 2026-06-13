package com.reyco.dasbx.commons.exception.strategy;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.commons.exception.DasbxException;

/**
 * 自定义异常处理顶级接口
 * @author reyco
 *
 */
public interface ExceptionStrategy {
	/**
	 * 是否支持
	 * @param type
	 * @return
	 */
	boolean support(String Type);
	/**
	 * 获取异常信息
	 * @param e  异常对象
	 * @return
	 */
	R getExceptionMessage(DasbxException e);
}
