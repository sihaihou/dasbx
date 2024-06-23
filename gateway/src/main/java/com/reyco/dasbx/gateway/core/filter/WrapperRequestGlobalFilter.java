package com.reyco.dasbx.gateway.core.filter;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Component
public class WrapperRequestGlobalFilter implements GlobalFilter {
	
	private static final Logger log = LoggerFactory.getLogger(WrapperRequestGlobalFilter.class);
	
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        if (HttpMethod.GET.matches(method)) {
            Map m = request.getQueryParams();
            logtrace(exchange, m.toString());
        }else{
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        String bodyString = new String(bytes, StandardCharsets.UTF_8);
                        logtrace(exchange, bodyString);
                        exchange.getAttributes().put("POST_BODY", bodyString);
                        DataBufferUtils.release(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory()
                                    .wrap(bytes);
                            return Mono.just(buffer);
                        });
                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };
                        return chain.filter(exchange.mutate().request(mutatedRequest)
                                .build());
                    });
        }  
        return chain.filter(exchange);
    }
	/**
     * 日志信息
     *
     * @param exchange
     * @param param    请求参数
     */
    private void logtrace(ServerWebExchange exchange, String param) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String id = serverHttpRequest.getHeaders().getFirst("Dasbx-Id");
        String key = serverHttpRequest.getHeaders().getFirst("Dasbx-Key");
        String timestamp = serverHttpRequest.getHeaders().getFirst("Dasbx-Timestamp");
        String contentSignature = serverHttpRequest.getHeaders().getFirst("Dasbx-Content-Signature");
        
        String path = serverHttpRequest.getURI().getPath();
        String method = serverHttpRequest.getMethodValue();
        String headers = serverHttpRequest.getHeaders().entrySet()
                .stream()
                .map(entry -> "            " + entry.getKey() + ": [" + String.join(";", entry.getValue()) + "]")
                .collect(Collectors.joining("\n"));
        log.info("\n" + "----------------             ----------------             ---------------->>\n" +
                        "HttpMethod : {}\n" +
                        "Uri        : {}\n" +
                        "Param      : {}\n" +
                        "Headers    : \n" +
                        "{}\n" +
                        "\"<<----------------             ----------------             ----------------"
                , method, path, param, headers);
    }
    private Map<String,Object> getParamsMap(String method,String parameters) {
    	Map<String,Object> paramterMap = new HashMap();
    	if(HttpMethod.GET.matches(method)) {
    		if(StringUtils.isBlank(parameters)) {
				return paramterMap;
			}
			StringTokenizer stringTokenizer = new StringTokenizer(parameters, "&");
			while(stringTokenizer.hasMoreTokens()) {
				String parameter = stringTokenizer.nextToken();
				String[] params = parameter.split("=");
				if(params.length==2) {
					paramterMap.put(params[0], params[1]);
				}
			}
    	}else {
    		if(StringUtils.isBlank(parameters)) {
				return paramterMap;
			}
    		Map<String,Object> map = JSON.parseObject(parameters, Map.class);
			return map;
    	}
    	return paramterMap;
    }
}
