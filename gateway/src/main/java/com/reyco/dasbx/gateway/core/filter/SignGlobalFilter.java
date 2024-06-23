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
import com.reyco.dasbx.gateway.core.sign.api.ApiSignature;
import com.reyco.dasbx.gateway.core.sign.api.GateWayResultCode;
import com.reyco.dasbx.gateway.core.utils.RequestUtils;

import reactor.core.publisher.Mono;

/**
 * 签名过滤器
 * @author reyco
 *
 */
public class SignGlobalFilter extends DefaultStaticExcludeIgnoringResources implements GlobalFilter,Ordered {
	
	private static final Logger log = LoggerFactory.getLogger(SignGlobalFilter.class);
	public final static String SIGN_FILTER_NAME = "sign-filter";
	private ApiSignature apiSignature;
	private int order = 0;
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		try {
			//静态资源/排除请求
			if(isStaticResourceOrExclude(request)) {
				return chain.filter(exchange);
			}
			//忽略请求
		   	if(ignoreInner(request)) {
		   		return chain.filter(exchange);
		   	}
			//前端传过来的签名
			String signatureSource = RequestUtils.getHeader(request, Constants.DASBX_SIGNATURE);
			if (StringUtils.isBlank(signatureSource)) {
				log.warn("SignGlobalFilter Signature fail:{}, path:{}", signatureSource,RequestUtils.getPath(request));
				return RequestUtils.response(exchange,R.fail(GateWayResultCode.DASBX_SIGNATURE.getMsg(), "非法请求"));
			}
			//后端生成的签名
			String signatureCalculative = apiSignature.getSign(request);
			//比对签名是否一致
			if (!signatureSource.equalsIgnoreCase(signatureCalculative)) {
				log.warn("SignGlobalFilter 计算的sign:{} , 前端传递的sign :{}, path:{}", signatureCalculative, signatureSource,RequestUtils.getPath(request));
				return RequestUtils.response(exchange,R.fail(GateWayResultCode.DASBX_KEY_SIGN.getMsg(), "非法请求"));
			}
			return chain.filter(exchange);
		} catch (Exception e) {
			log.error("SignGlobalFilter 鉴权未知异常, msg:{}, path:{}", e,RequestUtils.getPath(request));
			return RequestUtils.response(exchange,R.fail(GateWayResultCode.DASBX_ERROR.getMsg(), "非法请求"));
		}
	}
	public ApiSignature getApiSignature() {
		return apiSignature;
	}
	public void setApiSignature(ApiSignature apiSignature) {
		this.apiSignature = apiSignature;
	}
	@Override
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
}
