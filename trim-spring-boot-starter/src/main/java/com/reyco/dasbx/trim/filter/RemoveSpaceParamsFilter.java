package com.reyco.dasbx.trim.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.reyco.dasbx.trim.requset.RemoveSpaceHttpServletRequestWrapper;

/**
 * 移除空格的filter对象
 * @author reyco
 *
 */
public class RemoveSpaceParamsFilter implements Filter{

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		RemoveSpaceHttpServletRequestWrapper request = new RemoveSpaceHttpServletRequestWrapper((HttpServletRequest)servletRequest);
		chain.doFilter(request, servletResponse);
	}
	
}