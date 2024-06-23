package com.reyco.dasbx.gateway.core.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 静态/排除/忽略/资源
 * @author reyco
 *
 */
public interface StaticExclusionIgnoringResources {
	/**
	 * 是否静态资源
	 * @param request	请求对象
	 * @return
	 */
	boolean isStaticResource(ServerHttpRequest request);
	
	/**
	 * 是否排除资源
	 * @param request	请求对象
	 * @return
	 */
	boolean isExcludeResource(ServerHttpRequest request);
	
	/**
	 * 是否忽略资源
	 * @param request	请求对象
	 * @return
	 */
	Boolean ignoreInner(ServerHttpRequest request);
}
