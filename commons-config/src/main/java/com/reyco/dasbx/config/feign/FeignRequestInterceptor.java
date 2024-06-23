package com.reyco.dasbx.config.feign;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;

@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(FeignRequestInterceptor.class);
	
	@Override
	public void apply(feign.RequestTemplate template) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = requestAttributes.getRequest();
			Enumeration<String> headerNames = request.getHeaderNames();
			while(headerNames.hasMoreElements()) {
				String headerElementName = headerNames.nextElement();
				String headerElementValue = request.getHeader(headerElementName);
				template.header(headerElementName, headerElementValue);
			}
		}
	}

}
