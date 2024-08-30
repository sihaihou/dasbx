package com.reyco.dasbx.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class TrackFilter implements Filter{
	
	private static Logger logger = LoggerFactory.getLogger(TrackFilter.class);
	
	@Value("${spring.cloud.nacos.discovery.service:${spring.application.name:}}")
	private String applicationName;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String trackId = req.getHeader("trackId");             
		String parentSpanId = req.getHeader("parentSpanId");  
		String spanId = req.getHeader("spanId");  
		logger.debug("applicationName:{},trackId:{},parentSpanId:{},spanId:{}",applicationName,trackId,parentSpanId,spanId);
		chain.doFilter(req, response);
	}
}
