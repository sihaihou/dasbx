package com.reyco.dasbx.gateway.core.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.reyco.dasbx.id.core.IdGenerator;

import reactor.core.publisher.Mono;

@Component
public class TrackFilter implements GlobalFilter, Ordered {
	private static Logger logger = LoggerFactory.getLogger(TrackFilter.class);
	@Autowired
	IdGenerator<Long> idGenerator;

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders headers = exchange.getResponse().getHeaders();
		String trackId = headers.getFirst("trackId");
		String parentSpanId = headers.getFirst("parentSpanId");
		String spanId = headers.getFirst("spanId");
		if (StringUtils.isBlank(trackId)) {
			trackId = idGenerator.getGeneratorId().toString();
			parentSpanId = trackId;
			spanId = parentSpanId;
		}
		ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header("trackId", trackId)
                .header("parentSpanId", parentSpanId)
                .header("spanId", spanId)
                .build();
        return chain.filter(exchange.mutate().request(newRequest).build());
	}
}
