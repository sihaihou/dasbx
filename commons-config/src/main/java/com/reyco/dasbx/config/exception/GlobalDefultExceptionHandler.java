package com.reyco.dasbx.config.exception;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.exception.core.DasbxException;
import com.reyco.dasbx.config.exception.core.strategy.DelegationExceptionStrategy;

@ControllerAdvice
public class GlobalDefultExceptionHandler {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DelegationExceptionStrategy delegationExceptionStrategy;
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Object defultExcepitonHandler(Exception ex) {
		logger.error(ex.toString());
		ex.printStackTrace();
		try {
			// 自定义异常...
			if(ex instanceof DasbxException) {
				DasbxException dasbxException = (DasbxException)ex; 
				return delegationExceptionStrategy.getExceptionMessage(dasbxException);
			}
			// 数组溢出异常
			if(ex instanceof ArrayIndexOutOfBoundsException) {
				return getArrayIndexOutOfBoundsException(ex);
			}
			// 数字转换异常
			if(ex instanceof NumberFormatException) {
				return getNumberFormatException(ex);
			}
			// 非法参数
			if(ex instanceof IllegalArgumentException) {
				return getIllegalArgumentException(ex);
			}
			// 空指针异常
			if(ex instanceof NullPointerException) {
				return getNullPointerException(ex);
			}
			// SQL语句异常
			if(ex instanceof SQLException) {
				return getSQLException(ex);
			}
			// IO输入输出异常
			if(ex instanceof IOException) {
				return getIOException(ex);
			}
			return getException(ex);
		} catch (Exception e) {
			return getException(e);
		}
	}
	/**
	 * 非法参数异常
	 * @param ex
	 * @return
	 */
	private R getIllegalArgumentException(Exception ex) {
		logger.error("非法参数异常：" + ex.getMessage());
		IllegalArgumentException illegalArgumentException = (IllegalArgumentException) ex;
		return R.fail("非法参数异常：" + "msg:" + illegalArgumentException.getMessage(),"未知异常,请联系管理员...");
	}
	/**
	 * 空指针异常
	 * @param ex
	 * @return
	 */
	private R getNullPointerException(Exception ex) {
		logger.error("空指针异常：" + ex.getMessage());
		NullPointerException nullPointerException = (NullPointerException) ex;
		return R.fail("空指针异常：" + "msg:" + nullPointerException.getMessage(),"未知异常,请联系管理员...");
	}
	/**
	 * SQL语句异常
	 * @param ex
	 * @return
	 */
	private R getSQLException(Exception ex) {
		logger.error("SQL语句异常：" + ex.getMessage());
		SQLException qQLException = (SQLException) ex;
		return R.fail("SQL语句异常：" + "msg:" + qQLException.getMessage(),"未知异常,请联系管理员...");
	}
	/**
	 * 数字转换异常
	 * @param ex
	 * @return
	 */
	private R getNumberFormatException(Exception ex) {
		logger.error("数字转换异常：" + ex.getMessage());
		NumberFormatException numberFormatException = (NumberFormatException) ex;
		return R.fail("数字转换异常：" + "msg:" + numberFormatException.getMessage(),"未知异常,请联系管理员...");
	}
	/**
	 * 数组溢出异常
	 * @param ex
	 * @return
	 */
	private R getArrayIndexOutOfBoundsException(Exception ex) {
		logger.error("数组溢出异常：" + ex.getMessage());
		ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = (ArrayIndexOutOfBoundsException) ex;
		return R.fail("数组溢出异常：" + "msg:" + arrayIndexOutOfBoundsException.getMessage(),"未知异常,请联系管理员...");
	}
	/**
	 * IO输入输出异常
	 * @param ex
	 * @return
	 */
	private R getIOException(Exception ex) {
		logger.error("IO输入输出异常：" + ex.getMessage());
		IOException iOException = (IOException) ex;
		return R.fail("IO输入输出异常：" + "msg:" + iOException.getMessage(),"未知异常,请联系管理员...");
	}
	/**
	 * 系统异常
	 * @param ex
	 * @return
	 */
	private R getException(Exception ex) {
		logger.error("系统异常：" + ex);
		return R.fail("系统异常：msg:" + ex.getMessage(),"未知异常,请联系管理员...");
	}
}
