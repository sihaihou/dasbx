package com.reyco.dasbx.gateway.core.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import com.reyco.dasbx.gateway.core.constant.Constants;
import com.reyco.dasbx.gateway.core.exception.AuthenticationException;
import com.reyco.dasbx.gateway.core.model.R;
import com.reyco.dasbx.gateway.core.model.ExceptionCode;
import com.reyco.dasbx.gateway.core.model.SysAccountToken;
import com.reyco.dasbx.gateway.core.utils.RequestUtils;
import com.reyco.dasbx.gateway.core.utils.TokenUtils;

import reactor.core.publisher.Mono;

/**
 * 认证过滤器
 * @author reyco
 *
 */
public class AuthGlobalFilter extends DefaultStaticExcludeIgnoringResources implements GlobalFilter,Ordered{
	
	private static final Logger logger= LoggerFactory.getLogger(AuthGlobalFilter.class);
	public final static String AUTH_FILTER_NAME = "auth-filter";
	private int order = -1;
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		//静态资源/排除请求
		if(isStaticResourceOrExclude(request)) {
			return chain.filter(exchange);
		}
		//token
		String token = RequestUtils.getOption(request, Constants.TOKEN_NAME);
		String deviceId = RequestUtils.getOption(request, Constants.DEVICE_ID_NAME);
		String deviceType = RequestUtils.getOption(request, Constants.DEVICE_TYPE_NAME);
		if(StringUtils.isBlank(token) || StringUtils.isBlank(deviceId) || StringUtils.isBlank(deviceType)) {
			logger.warn("没有登录,no login! path:{}",RequestUtils.getPath(request));
			return RequestUtils.response(exchange, R.authFail(ExceptionCode.AUTHENTICATION_EXCEPTION.getCode(), null, ExceptionCode.AUTHENTICATION_EXCEPTION.getDesc(), "no login!"));
		}
		try {
			SysAccountToken accountToken = TokenUtils.getToken(deviceId, deviceType, token);
			if(accountToken==null) {
				logger.warn("登录失效,Login Invalid! path:{}",RequestUtils.getPath(request));
				return RequestUtils.response(exchange,R.authFail(ExceptionCode.AUTHENTICATION_EXCEPTION.getCode(), null, ExceptionCode.AUTHENTICATION_EXCEPTION.getDesc(), "Login Invalid!"));
			}
		} catch (AuthenticationException e) {
			return RequestUtils.response(exchange,R.authFail(ExceptionCode.AUTHENTICATION_EXCEPTION.getCode(), null, ExceptionCode.AUTHENTICATION_EXCEPTION.getDesc(), "Login Invalid!"));
		}
		return chain.filter(exchange);
	}
	@Override
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
