package com.reyco.dasbx.gateway.core.sign.api;

import org.springframework.http.server.reactive.ServerHttpRequest;

import com.reyco.dasbx.gateway.core.sign.SignatureException;

/**
 * Api签名
 * @author reyco
 *
 */
public interface ApiSignature {
	
	/**
	 * 获取签名
	 * @param serverHttpRequest
	 * @return
	 */
	String getSign(ServerHttpRequest serverHttpRequest) throws SignatureException;
	
}
