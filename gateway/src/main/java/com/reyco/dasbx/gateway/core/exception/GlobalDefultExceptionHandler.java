package com.reyco.dasbx.gateway.core.exception;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.reyco.dasbx.gateway.core.model.R;
import com.reyco.dasbx.gateway.core.model.ExceptionCode;
import com.reyco.dasbx.gateway.core.utils.RequestUtils;

import reactor.core.publisher.Mono;

@Order(-1)
@Component
public class GlobalDefultExceptionHandler implements ErrorWebExceptionHandler{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		if (exchange.getResponse().isCommitted()) {
			return Mono.error(ex);
		}
		// 认证异常
		if(ex instanceof AuthenticationException) {
			return RequestUtils.response(exchange, R.authFail(ex.getMessage()));
		}
		String message = "系统异常：msg:" + ex.getMessage();
		// 数组溢出异常
		if(ex instanceof ArrayIndexOutOfBoundsException) {
			message = "数组溢出异常：" + "msg:" + ex.getMessage();
		}
		// 数字转换异常
		if(ex instanceof NumberFormatException) {
			message = "数字转换异常：" + "msg:" + ex.getMessage();
		}
		// 非法参数
		if(ex instanceof IllegalArgumentException) {
			message = "非法参数异常：" + "msg:" + ex.getMessage();
		}
		// 空指针异常
		if(ex instanceof NullPointerException) {
			message = "空指针异常：" + "msg:" + ex.getMessage();
		}
		// SQL语句异常
		if(ex instanceof SQLException) {
			message = "SQL语句异常：" + "msg:" + ex.getMessage();
		}
		// IO输入输出异常
		if(ex instanceof IOException) {
			message = "IO输入输出异常：" + "msg:" + ex.getMessage();
		}
		return RequestUtils.response(exchange, R.fail(message, ExceptionCode.SYSTEM_EXCEPTION.getDesc()));
	}
}
