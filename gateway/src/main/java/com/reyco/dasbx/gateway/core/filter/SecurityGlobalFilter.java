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
import com.reyco.dasbx.gateway.core.model.R;
import com.reyco.dasbx.gateway.core.utils.RequestUtils;

import reactor.core.publisher.Mono;

/**
 * 安全过滤器
 * @author reyco
 *
 */
public class SecurityGlobalFilter extends DefaultStaticExcludeIgnoringResources implements GlobalFilter,Ordered{
	private static final Logger logger= LoggerFactory.getLogger(SecurityGlobalFilter.class);
	public final static String SECURITY_FILTER_NAME = "security-filter";
	private int order = -2;
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		//静态资源/排除请求
		if(isStaticResourceOrExclude(request)) {
			return chain.filter(exchange);
		}
		//accept
		String accept = RequestUtils.getOption(request, Constants.ACCEPT_NAME);
		if(StringUtils.isBlank(accept)) {
			logger.warn("非法请求Accept! path:{}",RequestUtils.getPath(request));
			return RequestUtils.response(exchange,R.fail("非法请求Accept!", "非法请求"));
		}
		//code
		String token = RequestUtils.getOption(request, Constants.TOKEN_NAME);
		String code = RequestUtils.getOption(request, Constants.CODE_NAME);
		if(StringUtils.isBlank(token) && StringUtils.isBlank(code)) {
			logger.warn("非法请求Code! path:{}",RequestUtils.getPath(request));
			return RequestUtils.response(exchange,R.fail("非法请求Code!", "非法请求"));
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
