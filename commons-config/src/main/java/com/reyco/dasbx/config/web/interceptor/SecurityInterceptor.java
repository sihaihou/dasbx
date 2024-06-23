package com.reyco.dasbx.config.web.interceptor;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.config.utils.TokenUtils;
import com.reyco.dasbx.model.constants.Constants;

/**
 * 验证Accept与code信息
 * @author reyco
 *
 */
@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
	public final static String INTERCEPTOR_NAME = "security";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		if(!validateAccept(request, response)) {
			return false;
		}
		if(!validateCode(request, response)) {
			return false;
		}
		return true;
	}
	private Boolean validateAccept(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String accept = request.getHeader(Constants.ACCEPT_NAME);
		if (StringUtils.isBlank(accept)) {
			R<?> r = R.fail("非法请求Accept","非法请求");
			OutputStream os = response.getOutputStream();
			String json = JSONObject.toJSONString(r);
			os.write(json.getBytes("UTF-8"));
			os.flush();
			return false;
		}
		return true;
	}
	private Boolean validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(!request.getServletPath().equalsIgnoreCase(Constants.CODE_API)) {
			String token = TokenUtils.getTokenString(request);
			String code = TokenUtils.getCodeString(request);
			if(StringUtils.isBlank(token) && StringUtils.isBlank(code)) {
				logger.info("token:"+token+",code:"+code);
				R<?> r = R.fail("非法请求Code","非法请求");
				OutputStream os = response.getOutputStream();
				String json = JSONObject.toJSONString(r);
				os.write(json.getBytes("UTF-8"));
				os.flush();
				return false;
			}
		}
		return true;
	}

}
